package io.narsha.smartpage.spring.core.web;

import static org.assertj.core.api.Assertions.assertThat;

import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.spring.core.configuration.SmartPageResolverWebConfiguration;
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
    classes = {
      SmartPageSpringTestApplication.class,
      SmartPageResolverWebConfiguration.class,
      WebConfig.class
    },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResolverTest {

  @Value(value = "${local.server.port}")
  private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private AtomicReference<SmartPageQuery<Person>> reference;

  @Test
  void noFilterNoPage() {
    assertThat(call(null).getStatusCode()).isEqualTo(HttpStatus.OK);

    final var query = reference.get();
    assertPageAndTarget(query);
    assertThat(query.orders()).isEmpty();
    assertThat(query.filters()).isEmpty();
  }

  @Test
  void noFilterWithPageAndSort() {
    assertThat(call("?page=10&size=300&sort=id,asc&sort=firstName,desc").getStatusCode())
        .isEqualTo(HttpStatus.OK);
    final var query = reference.get();
    assertPageAndTarget(query, 10, 300);
    final var sort = Map.of("id", "ASC", "first_name", "DESC");
    assertThat(query.orders()).isEqualTo(sort);
    assertThat(query.filters()).isEmpty();
  }

  @Test
  void noFilterWithPageAndUnknownSort() {
    assertThat(call("?page=10&size=300&sort=id,asc&sort=test,desc").getStatusCode())
        .isEqualTo(HttpStatus.OK);
    final var query = reference.get();
    assertPageAndTarget(query, 10, 300);

    final var sort = Map.of("id", "ASC");
    assertThat(query.orders()).isEqualTo(sort);
    assertThat(query.filters()).isEmpty();
  }

  @Test
  void validFilters() {
    assertThat(
            call("?id=1&firstName=toto&filter=id,equals&filter=firstName,equals").getStatusCode())
        .isEqualTo(HttpStatus.OK);
    final var query = reference.get();
    assertPageAndTarget(query);

    assertThat(query.orders()).isEmpty();

    assertThat(query.filters()).hasSize(2);

    var opt =
        query.filters().stream().filter(e -> e.dataSourceProperty().equals("id")).findFirst().get();
    assertThat(opt.value()).isEqualTo(1L);

    opt =
        query.filters().stream()
            .filter(e -> e.dataSourceProperty().equals("first_name"))
            .findFirst()
            .get();
    assertThat(opt.value()).isEqualTo("toto");
  }

  @Test
  void withIgnoreFilterAndValidFilters() {
    assertThat(call("?id=1&role=toto&filter=id,equals").getStatusCode()).isEqualTo(HttpStatus.OK);
    final var query = reference.get();
    assertPageAndTarget(query);

    assertThat(query.orders()).isEmpty();

    assertThat(query.filters()).hasSize(1);
    var opt =
        query.filters().stream().filter(e -> e.dataSourceProperty().equals("id")).findFirst().get();
    assertThat(opt.value()).isEqualTo(1L);
  }

  @Test
  void withDefaultFilterAndUnknownFilter() {
    assertThat(call("?id=1&firstName=toto&filter=id,equals&filter=truc,equals").getStatusCode())
        .isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void withInvalidFilterType() {
    var response = call("?id=1&firstName=toto&filter=id,equals&filter=firstName,truc");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<String> call(String param) {
    return this.restTemplate.getForEntity(
        "http://localhost:" + port + "/test" + (param == null ? "" : param), String.class);
  }

  private void assertPageAndTarget(SmartPageQuery<Person> query) {
    this.assertPageAndTarget(query, 0, 20);
  }

  private void assertPageAndTarget(SmartPageQuery<Person> query, Integer page, Integer size) {
    assertThat(query.page()).isEqualTo(page);
    assertThat(query.size()).isEqualTo(size);
    assertThat(query.targetClass()).isEqualTo(Person.class);
  }
}
