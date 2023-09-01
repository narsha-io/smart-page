package io.narsha.smartpage.spring.core.web;

import static org.assertj.core.api.Assertions.assertThat;

import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.filters.EqualsFilter;
import io.narsha.smartpage.spring.test.SmartPageSpringTestApplication;
import io.narsha.smartpage.spring.test.model.Person;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAutoConfiguration
@EnableWebMvc
@SpringBootTest(
    classes = {SmartPageSpringTestApplication.class, WebConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResolverTest {

  @Value(value = "${local.server.port}")
  private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private AtomicReference<PaginatedFilteredQuery<Person>> reference;

  @Test
  public void noFilterNoPage() {
    assertThat(call(null).getStatusCode()).isEqualTo(HttpStatus.OK);

    final var query = reference.get();
    assertPageAndTarget(query);
    assertThat(query.getOrders()).isEmpty();
    assertThat(query.getFilters()).isEmpty();
  }

  @Test
  public void noFilterWithPageAndSort() {
    assertThat(call("?page=10&size=300&sort=id,asc&sort=firstName,desc").getStatusCode())
        .isEqualTo(HttpStatus.OK);
    final var query = reference.get();
    assertPageAndTarget(query, 10, 300);
    final var sort = Map.of("id", "asc", "firstName", "desc");
    assertThat(query.getOrders()).isEqualTo(sort);
    assertThat(query.getFilters()).isEmpty();
  }

  @Test
  public void noFilterWithPageAndUnknownSort() {
    assertThat(call("?page=10&size=300&sort=id,asc&sort=test,desc").getStatusCode())
        .isEqualTo(HttpStatus.OK);
    final var query = reference.get();
    assertPageAndTarget(query, 10, 300);

    final var sort = Map.of("id", "asc");
    assertThat(query.getOrders()).isEqualTo(sort);
    assertThat(query.getFilters()).isEmpty();
  }

  @Test
  public void validFilters() {
    assertThat(
            call("?id=1&firstName=toto&filter=id,equals&filter=firstName,equals").getStatusCode())
        .isEqualTo(HttpStatus.OK);
    final var query = reference.get();
    assertPageAndTarget(query);

    assertThat(query.getOrders()).isEmpty();

    assertThat(query.getFilters()).hasSize(2);
    assertThat(query.getFilters()).containsKey("id");
    var filter = query.getFilters().get("id");
    assertThat(filter).isInstanceOf(EqualsFilter.class);
    assertThat(filter.getValue()).isEqualTo(1L);

    assertThat(query.getFilters()).containsKey("firstName");
    filter = query.getFilters().get("firstName");
    assertThat(filter).isInstanceOf(EqualsFilter.class);
    assertThat(filter.getValue()).isEqualTo("toto");
  }

  @Test
  public void withDefaultFilterAndUnknownFilter() {
    assertThat(call("?id=1&firstName=toto&filter=id,equals&filter=truc,equals").getStatusCode())
        .isEqualTo(HttpStatus.OK);
    final var query = reference.get();
    assertPageAndTarget(query);

    assertThat(query.getOrders()).isEmpty();

    assertThat(query.getFilters()).hasSize(2);
    assertThat(query.getFilters()).containsKey("id");
    var filter = query.getFilters().get("id");
    assertThat(filter).isInstanceOf(EqualsFilter.class);
    assertThat(filter.getValue()).isEqualTo(1L);

    assertThat(query.getFilters()).containsKey("firstName");
    filter = query.getFilters().get("firstName");
    assertThat(filter).isInstanceOf(EqualsFilter.class);
    assertThat(filter.getValue()).isEqualTo("toto");
  }

  @Test
  public void withInvalidFilterType() {
    var response = call("?id=1&firstName=toto&filter=id,equals&filter=firstName,truc");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("Unrecognized filter type : truc");
  }

  private ResponseEntity<String> call(String param) {
    return this.restTemplate.getForEntity(
        "http://localhost:" + port + "/test" + (param == null ? "" : param), String.class);
  }

  private void assertPageAndTarget(PaginatedFilteredQuery<Person> query) {
    this.assertPageAndTarget(query, 0, 20);
  }

  private void assertPageAndTarget(
      PaginatedFilteredQuery<Person> query, Integer page, Integer size) {
    assertThat(query.getPage()).isEqualTo(page);
    assertThat(query.getSize()).isEqualTo(size);
    assertThat(query.getTargetClass()).isEqualTo(Person.class);
  }
}
