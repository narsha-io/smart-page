package io.narsha.smartpage.spring.core.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.exceptions.UnknownFilterException;
import io.narsha.smartpage.core.filters.Filter;
import io.narsha.smartpage.core.filters.FilterParser;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PaginatedFilteredQueryResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;
    private final PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;
    private final PathVariableMapMethodArgumentResolver pathVariableMapMethodArgumentResolver;

    public PaginatedFilteredQueryResolver(ObjectMapper objectMapper, PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver) {
        this.objectMapper = objectMapper;
        this.pageableHandlerMethodArgumentResolver = pageableHandlerMethodArgumentResolver;
        this.pathVariableMapMethodArgumentResolver = new PathVariableMapMethodArgumentResolver();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PaginatedFilteredQuery.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final var targetClass = (Class<?>) ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0];

        final var parameters = new HashMap<>(webRequest.getParameterMap());

        final var httpFilters = parameters.getOrDefault("filter", new String[0]);

        addPathVariableIntoFilterMap(parameter, mavContainer, webRequest, binderFactory, parameters);
        removeUnknownFilters(parameters, targetClass);

        final var pageable = pageableHandlerMethodArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        final var filters = getFilters(parameters, httpFilters, targetClass);

        final var paginatedFilteredQuery = new PaginatedFilteredQuery<>(targetClass, pageable.getPageNumber(), pageable.getPageSize());
        paginatedFilteredQuery.getOrders().putAll(extractSort(targetClass, pageable));
        paginatedFilteredQuery.getFilters().putAll(filters);

        return paginatedFilteredQuery;
    }

    private Map<String, FilterParser> getFilters(Map<String, String[]> parameters, String[] httpFilters, Class<?> targetClass) throws UnknownFilterException {
        //TODO make safe
        var filters = Stream.of(httpFilters)
                .map(e -> e.split(","))
                .collect(Collectors.toMap(e -> e[0], e -> e[1]));

        final var result = new HashMap<String, FilterParser>();

        for (var entry : parameters.entrySet()) {
            var filter = filters.getOrDefault(entry.getKey(), Filter.EQUALS.name());

            if (!isValidFilter(filter)) {
                throw new UnknownFilterException(filter);
            }

            // do not care about unknown property (can be used for something else in the http query)
            getPropertyClass(entry.getKey(), targetClass)
                    .ifPresent(propertyClass -> {
                        var parser = Filter.valueOf(filter.toUpperCase()).getParser().get();
                        parser.parse(objectMapper, propertyClass, entry.getValue());
                        result.put(entry.getKey(), parser);
                    });
        }

        return result;
    }

    private boolean isValidFilter(String name) {
        return Stream.of(Filter.values()).anyMatch(v -> Objects.equals(v.name().toLowerCase(), name.toLowerCase()));
    }

    private Optional<Class<?>> getPropertyClass(String property, Class<?> targetClass) {
        return Optional.ofNullable(ReflectionUtils.findField(targetClass, property))
                .map(Field::getType);
    }

    private void addPathVariableIntoFilterMap(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory, HashMap<String, String[]> parameters) throws Exception {
        final var pathVariableMap = pathVariableMapMethodArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        if (pathVariableMap instanceof Map map) {
            map.forEach((key, value) -> parameters.put(String.valueOf(key), new String[]{String.valueOf(value)}));
        }
    }

    private Map<String, String> extractSort(Class<?> targetClass, Pageable pageable) {
        var res = pageable.getSort().stream().collect(Collectors.toMap(Sort.Order::getProperty, sort -> sort.getDirection().name().toLowerCase()));
        res.entrySet().removeIf(e -> ReflectionUtils.findField(targetClass, e.getKey()) == null);
        return res;
    }

    private void removeUnknownFilters(Map<String, ?> parameters, Class<?> targetClass) {
        parameters.entrySet().removeIf(entry -> ReflectionUtils.findField(targetClass, entry.getKey()) == null);
    }
}
