package io.narsha.smartpage.spring.core;

import static io.narsha.smartpage.core.utils.HeaderUtils.X_TOTAL_COUNT;

import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.core.SmartPageResult;
import io.narsha.smartpage.core.utils.HeaderUtils;
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

    headers.set(
        HttpHeaders.LINK,
        HeaderUtils.generateHeader(
            ServletUriComponentsBuilder.fromCurrentRequest().toUriString(), page, size, result));
    headers.set(X_TOTAL_COUNT, String.valueOf(result.total()));
    return headers;
  }

  private String buildURI(Integer page) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .replaceQueryParam("page", page)
        .build()
        .toUriString();
  }
}
