package io.narsha.smartpage.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class PaginatedFilteredQuery<T> {

    private final Class<T> targetClass;
    private final String query = "select id, name from countries";

    public PaginatedFilteredQuery(Class<T> targetClass) {
        this.targetClass = targetClass;
    }


    public Class<T> getTargetClass() {
        return this.targetClass;
    }

    public String getQuery() {
        return query;
    }
}
