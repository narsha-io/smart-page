package io.narsha.smartpage.core.utils;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

  @ParameterizedTest
  @MethodSource("firstPageSource")
  void testFirstPage(Integer current, Integer value) {
    var opt = HeaderUtils.firstPage(current);
    if (value == null) {
      Assertions.assertThat(opt).isEmpty();
    } else {
      Assertions.assertThat(opt).contains(value);
    }
  }

  @ParameterizedTest
  @MethodSource("prevPageSource")
  void testPrevPage(Integer current, Integer value) {
    var opt = HeaderUtils.previousPage(current);
    if (value == null) {
      Assertions.assertThat(opt).isEmpty();
    } else {
      Assertions.assertThat(opt).contains(value);
    }
  }

  @ParameterizedTest
  @MethodSource("maxPageSource")
  void testMaxPage(Integer pageSize, Integer totalElement, Integer maxPage) {
    var value = HeaderUtils.maxPage(pageSize, totalElement);
    Assertions.assertThat(value).isEqualTo(maxPage);
  }

  @ParameterizedTest
  @MethodSource("lastPageSource")
  void testLastPage(Integer current, Integer pageSize, Integer totalElement, Integer maxPage) {
    var opt = HeaderUtils.lastPage(current, pageSize, totalElement);
    if (maxPage == null) {
      Assertions.assertThat(opt).isEmpty();
    } else {
      Assertions.assertThat(opt).contains(maxPage);
    }
  }

  @ParameterizedTest
  @MethodSource("nextPageSource")
  void testNextPage(Integer current, Integer pageSize, Integer totalElement, Integer maxPage) {
    var opt = HeaderUtils.nextPage(current, pageSize, totalElement);
    if (maxPage == null) {
      Assertions.assertThat(opt).isEmpty();
    } else {
      Assertions.assertThat(opt).contains(maxPage);
    }
  }

  private static Stream<Arguments> firstPageSource() {
    return Stream.of(Arguments.of(0, null), Arguments.of(1, 0), Arguments.of(10, 0));
  }

  private static Stream<Arguments> prevPageSource() {
    return Stream.of(Arguments.of(0, null), Arguments.of(1, 0), Arguments.of(10, 9));
  }

  private static Stream<Arguments> maxPageSource() {
    return Stream.of(Arguments.of(10, 10, 0), Arguments.of(10, 20, 1), Arguments.of(10, 15, 1));
  }

  private static Stream<Arguments> lastPageSource() {
    return Stream.of(
        Arguments.of(0, 10, 10, null), Arguments.of(0, 10, 20, 1), Arguments.of(0, 10, 15, 1));
  }

  private static Stream<Arguments> nextPageSource() {
    return Stream.of(
        Arguments.of(0, 10, 10, null),
        Arguments.of(0, 10, 20, 1),
        Arguments.of(0, 10, 15, 1),
        Arguments.of(1, 10, 15, null));
  }
}
