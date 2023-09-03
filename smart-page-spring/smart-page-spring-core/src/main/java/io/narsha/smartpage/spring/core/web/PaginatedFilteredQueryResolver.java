package io.narsha.smartpage.spring.core.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.filters.EqualsFilter;
import io.narsha.smartpage.core.filters.FilterFactoryRegistrationService;
import io.narsha.smartpage.core.filters.FilterParser;
import io.narsha.smartpage.core.utils.ReflectionUtils;
import io.narsha.smartpage.core.utils.ResolverUtils;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver;

@Component
@RequiredArgsConstructor
public class PaginatedFilteredQueryResolver implements HandlerMethodArgumentResolver {

  private final ObjectMapper objectMapper;
  private final PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;
  private final FilterFactoryRegistrationService filterFactoryRegistrationService;
  private PathVariableMapMethodArgumentResolver pathVariableMapMethodArgumentResolver =
      new PathVariableMapMethodArgumentResolver();

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(PaginatedFilteredQuery.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws Exception {

    final var targetClass =
        (Class<?>)
            ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0];

    final var parameters = new HashMap<>(webRequest.getParameterMap());
    addPathVariableIntoFilterMap(parameter, mavContainer, webRequest, binderFactory, parameters);

    final var filtersParser = getFiltersParser(parameters, targetClass);

    final var pageable =
        pageableHandlerMethodArgumentResolver.resolveArgument(
            parameter, mavContainer, webRequest, binderFactory);

    final var paginatedFilteredQuery =
        new PaginatedFilteredQuery<>(
            targetClass,
            new HashMap<>(),
            new HashMap<>(),
            pageable.getPageNumber(),
            pageable.getPageSize());
    paginatedFilteredQuery.orders().putAll(extractSort(targetClass, pageable));
    paginatedFilteredQuery.filters().putAll(filtersParser);

    return paginatedFilteredQuery;
  }

  private void addPathVariableIntoFilterMap(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory,
      HashMap<String, String[]> parameters)
      throws Exception {
    final var pathVariableMap =
        pathVariableMapMethodArgumentResolver.resolveArgument(
            parameter, mavContainer, webRequest, binderFactory);
    if (pathVariableMap instanceof Map<?, ?> map) {
      map.forEach(
          (key, value) ->
              parameters.put(String.valueOf(key), new String[] {String.valueOf(value)}));
    }
  }

  private Map<String, String> extractSort(Class<?> targetClass, Pageable pageable) {
    return pageable.getSort().stream()
        .filter(e -> ReflectionUtils.getFieldClass(targetClass, e.getProperty()).isPresent())
        .map(
            e ->
                Pair.of(
                    ResolverUtils.getQueryProperty(targetClass, e.getProperty()).orElse(null),
                    e.getDirection().name()))
        .filter(e -> e.getKey() != null)
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
  }

  private Map<String, FilterParser<?, ?>> getFiltersParser(
      Map<String, String[]> parameters, Class<?> targetClass) {
    final var res = new HashMap<String, FilterParser<?, ?>>();

    var parsers = getParser(targetClass, parameters.getOrDefault("filter", new String[0]));

    for (var entry : parameters.entrySet()) {
      if (ResolverUtils.isIgnoredField(targetClass, entry.getKey())) {
        continue;
      }
      ReflectionUtils.getFieldClass(targetClass, entry.getKey())
          .ifPresent(
              type -> {
                var parser = parsers.getOrDefault(entry.getKey(), new EqualsFilter<>(type));
                parser.parse(objectMapper, entry.getValue());
                res.put(entry.getKey(), parser);
              });
    }

    return res;
  }

  private Map<String, FilterParser<?, ?>> getParser(Class<?> targetClass, String... filters) {
    final var propertyParser = new HashMap<String, FilterParser<?, ?>>();

    for (var filter : filters) {
      var split = filter.split(",");

      var javaProperty = split[0];
      var filterType = split[1];
      ReflectionUtils.getFieldClass(targetClass, javaProperty)
          .flatMap(type -> this.filterFactoryRegistrationService.get(type, filterType))
          .ifPresentOrElse(
              parser -> propertyParser.put(javaProperty, parser),
              () -> {
                throw new IllegalArgumentException();
              });
    }

    return propertyParser;
  }
}
