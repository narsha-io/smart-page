package io.narsha.smartpage.spring.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.core.SmartPageResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SmartPageTest {

  QueryExecutor queryExecutor;
  SmartPageQuery paginatedFilteredQuery;

  SmartPage smartPage;

  @BeforeEach
  void init() {
    queryExecutor = mock();
    paginatedFilteredQuery = mock();
    smartPage = new SmartPage(queryExecutor);
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }

  @Test
  void smartPageNoContent() {
    when(queryExecutor.execute(paginatedFilteredQuery))
        .thenReturn(new SmartPageResult<>(List.of(), 0));
    var res = smartPage.asResponseEntity(paginatedFilteredQuery);
    assertThat(res).isNotNull();
    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(res.getBody()).isNull();
    assertThat(res.getHeaders()).isEmpty();
  }

  @Test
  void allData() {
    when(paginatedFilteredQuery.page()).thenReturn(0);
    when(paginatedFilteredQuery.size()).thenReturn(10);
    final var data = List.of(1L);
    when(queryExecutor.execute(paginatedFilteredQuery)).thenReturn(new SmartPageResult<>(data, 10));
    var res = smartPage.asResponseEntity(paginatedFilteredQuery);
    assertThat(res).isNotNull();
    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(res.getBody()).isEqualTo(data);
    assertThat(res.getHeaders()).isEmpty();
  }

  @Test
  void hasMorePage() {
    when(paginatedFilteredQuery.page()).thenReturn(0);
    when(paginatedFilteredQuery.size()).thenReturn(5);
    final var data = List.of(1L);
    when(queryExecutor.execute(paginatedFilteredQuery)).thenReturn(new SmartPageResult<>(data, 10));
    var res = smartPage.asResponseEntity(paginatedFilteredQuery);
    assertThat(res).isNotNull();
    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(res.getBody()).isEqualTo(data);
    assertThat(res.getHeaders()).isEmpty();
  }

  @Test
  void hasPageBefore() {
    when(paginatedFilteredQuery.page()).thenReturn(1);
    when(paginatedFilteredQuery.size()).thenReturn(5);
    final var data = List.of(1L);
    when(queryExecutor.execute(paginatedFilteredQuery)).thenReturn(new SmartPageResult<>(data, 10));
    var res = smartPage.asResponseEntity(paginatedFilteredQuery);
    assertThat(res).isNotNull();
    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(res.getBody()).isEqualTo(data);
    assertThat(res.getHeaders()).isEmpty();
  }
}
