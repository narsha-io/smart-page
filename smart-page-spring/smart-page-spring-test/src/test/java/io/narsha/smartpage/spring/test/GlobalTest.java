package io.narsha.smartpage.spring.test;

import static org.assertj.core.api.Assertions.assertThat;

import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.spring.test.model.Person;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAutoConfiguration
@EnableWebMvc
@SpringBootTest(
    classes = SmartPageSpringTestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GlobalTest {

  @Autowired AtomicReference<PaginatedFilteredQuery<Person>> ref;

  @Value(value = "${local.server.port}")
  private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void validReferenceTest() {
    var res = this.restTemplate.getForEntity("http://localhost:" + port + "/test", String.class);
    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(res.getBody()).isEqualTo("ok");
    assertThat(ref.get()).isNotNull();
  }
}
