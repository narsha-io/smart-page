package io.narsha.smartpage.core.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class HeaderUtilsTest {

  @Test
  void testPageAtBeginning() {
    var res =
        HeaderUtils.buildURI(
            "http://localhost:8080/endpoint?page=5&size=10&test=value&filter=test,equals", 10);
    Assertions.assertThat(res)
        .isEqualTo("http://localhost:8080/endpoint?page=10&size=10&test=value&filter=test,equals");
  }

  @Test
  void testPageInMiddle() {
    var res =
        HeaderUtils.buildURI(
            "http://localhost:8080/endpoint?size=10&page=5&test=value&filter=test,equals", 10);
    Assertions.assertThat(res)
        .isEqualTo("http://localhost:8080/endpoint?size=10&page=10&test=value&filter=test,equals");
  }

  @Test
  void testPageNotPresentWithoutParam() {
    var res = HeaderUtils.buildURI("http://localhost:8080/endpoint", 10);
    Assertions.assertThat(res).isEqualTo("http://localhost:8080/endpoint?page=10");
  }

  @Test
  void testPageNotPresentWithParam() {
    var res =
        HeaderUtils.buildURI(
            "http://localhost:8080/endpoint?size=10&test=value&filter=test,equals", 10);
    Assertions.assertThat(res)
        .isEqualTo("http://localhost:8080/endpoint?size=10&test=value&filter=test,equals&page=10");
  }
}
