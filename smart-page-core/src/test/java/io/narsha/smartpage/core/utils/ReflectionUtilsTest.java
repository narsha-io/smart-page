package io.narsha.smartpage.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ReflectionUtilsTest {

  @Test
  void testValidField() {
    var idClass = ReflectionUtils.getFieldClass(Person.class, "id");
    assertThat(idClass).hasValue(Long.class);

    var firstNameClass = ReflectionUtils.getFieldClass(Person.class, "firstName");
    assertThat(firstNameClass).hasValue(String.class);
  }

  @Test
  void testInvalidField() {
    var idClass = ReflectionUtils.getFieldClass(Person.class, "test");
    assertThat(idClass).isEmpty();
  }

  private static class Person {
    private Long id;
    private String firstName;
  }
}
