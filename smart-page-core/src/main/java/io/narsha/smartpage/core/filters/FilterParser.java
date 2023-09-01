package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class FilterParser {

    public abstract void parse(ObjectMapper objectMapper, Class<?> targetClass, String[] value);

    public abstract String getSQLFragment(String property);

    public abstract Object getValue();
}
