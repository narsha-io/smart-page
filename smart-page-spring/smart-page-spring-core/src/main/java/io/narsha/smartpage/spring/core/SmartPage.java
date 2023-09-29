package io.narsha.smartpage.spring.core;

import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.core.SmartPageResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
public class SmartPage {

  // TODO manage multi executor depend on the annotation choice
  private final QueryExecutor executor;

  /**
   * generate a spring response entity which contains the response body and some http header RFC988
   * more information https://datatracker.ietf.org/doc/html/rfc5988#page-6
   *
   * @param query the query to execute
   * @return the http response entity
   * @param <T> targeted DTO type
   */
  public <T> ResponseEntity<List<T>> asResponseEntity(SmartPageQuery<T> query) {
    var result = executor.execute(query);
    if (CollectionUtils.isEmpty(result.data())) {
      return ResponseEntity.noContent().build();
    }

    final var headers = generatePaginationHeaders(query.page(), query.size(), result);
    return ResponseEntity.ok().headers(headers).body(result.data());
  }

  private <T> HttpHeaders generatePaginationHeaders(
      Integer page, Integer size, SmartPageResult<T> result) {
    final var headers = new HttpHeaders();
    final var link = new StringBuilder();
    if (page + 1 < result.total() / size) { // check here
      link.append("<").append(buildURI(page + 1)).append(">; rel=\"next\",");
    }
    if (page > 1) {
      link.append("<").append(buildURI(page - 1)).append(">; rel=\"prev\",");
    }

    var lastPage = 0;
    if (result.total() / size > 0) {
      lastPage = (result.total() / size) - 1;
    }

    link.append("<").append(buildURI(lastPage)).append(">; rel=\"prev\",");
    link.append("<").append(buildURI(0)).append(">; rel=\"first\",");

    headers.set(HttpHeaders.LINK, link.toString());
    headers.set("X-Total-Count", String.valueOf(result.total()));
    return headers;
  }

  private String buildURI(Integer page) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .replaceQueryParam("page", page)
        .build()
        .toUriString();
  }
}
