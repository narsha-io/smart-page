package io.narsha.smartpage.spring.core.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.PropertyFilter;
import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.core.exceptions.InternalException;
import io.narsha.smartpage.core.filters.Filter;
import io.narsha.smartpage.core.filters.FilterRegistrationService;
import io.narsha.smartpage.core.utils.ReflectionUtils;
import io.narsha.smartpage.core.utils.ResolverUtils;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver;

/** PaginatedFilteredQueryResolver which convert a http request into PaginatedFilteredQuery */
@RequiredArgsConstructor
public class SmartPageQueryResolver<T extends Filter> implements HandlerMethodArgumentResolver {

  private final ObjectMapper objectMapper;
  private final PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;
  private final FilterRegistrationService<T> filterRegistrationService;
  private PathVariableMapMethodArgumentResolver pathVariableMapMethodArgumentResolver =
      new PathVariableMapMethodArgumentResolver();

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(SmartPageQuery.class);
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

    final var filtersParser = getFiltersValue(parameters, targetClass);

    final var pageable =
        pageableHandlerMethodArgumentResolver.resolveArgument(
            parameter, mavContainer, webRequest, binderFactory);

    final var paginatedFilteredQuery =
        new SmartPageQuery<>(
            targetClass,
            new HashSet<>(),
            new HashMap<>(),
            pageable.getPageNumber(),
            pageable.getPageSize());
    paginatedFilteredQuery.orders().putAll(extractSort(targetClass, pageable));
    paginatedFilteredQuery.filters().addAll(filtersParser);

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

  private Set<PropertyFilter> getFiltersValue(
      Map<String, String[]> parameters, Class<?> targetClass) {

    final var filters = new HashMap<String, Filter>();
    final var equalsFilter =
        filterRegistrationService.get("equals").orElseThrow(InternalException::new);

    parameters.entrySet().stream()
        .filter(e -> e.getKey().equals("filter"))
        .map(Map.Entry::getValue)
        .flatMap(Stream::of)
        .forEach(
            filter -> {
              var split = filter.split(",");
              var smartPageFilter =
                  filterRegistrationService.get(split[1]).orElseThrow(InternalException::new);

              if (ResolverUtils.getQueryProperty(targetClass, split[0]).isEmpty()) {
                throw new InternalException();
              }
              filters.put(split[0], smartPageFilter);
            });

    final var res = new HashSet<PropertyFilter>();

    for (var entry : parameters.entrySet()) {
      var javaProperty = entry.getKey();
      if (ResolverUtils.isIgnoredField(targetClass, javaProperty)) {
        continue;
      }
      ReflectionUtils.getFieldClass(targetClass, javaProperty)
          .ifPresent(
              type -> {
                final var filter = filters.getOrDefault(javaProperty, equalsFilter);
                final var value =
                    filter.getParsedValue(objectMapper, type, parameters.get(javaProperty));

                var targetName =
                    ResolverUtils.getQueryProperty(targetClass, javaProperty).orElse(javaProperty);
                res.add(new PropertyFilter(targetName, value, filter.getFilterAlias()));
              });
    }
    return res;
  }
}
