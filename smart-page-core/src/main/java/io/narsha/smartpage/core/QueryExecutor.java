package io.narsha.smartpage.core;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface QueryExecutor {

    <T> Pair<List<T>, Long> execute(PaginatedFilteredQuery<T> paginatedFilteredQuery, RowMapper rowMapper);
}
