package io.narsha.smartpage.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.narsha.smartpage.core.annotations.DataTable;
import io.narsha.smartpage.core.annotations.DataTableIgnore;
import io.narsha.smartpage.core.annotations.DataTableProperty;
import io.narsha.smartpage.core.exceptions.InternalException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AnnotationUtilsTest {

  @Test
  void getClassAnnotationValue() {
    var res =
        AnnotationUtils.getClassAnnotationValue(Person.class, DataTable.class, DataTable::value);
    assertThat(res).isEqualTo("select 1");
  }

  @Test
  void getClassAnnotationValueFail() {
    assertThrows(
        InternalException.class,
        () ->
            AnnotationUtils.getClassAnnotationValue(
                Person.class, Test.class, Test::annotationType));
  }

  @Test
  void getFieldAnnotationValue() {
    var res =
        AnnotationUtils.getFieldAnnotationValue(
            Person.class, "firstName", DataTableProperty.class, DataTableProperty::columnName);
    assertThat(res).isEqualTo(Optional.of("first_name"));
  }

  @Test
  void getFieldAnnotationValueUnknownField() {
    assertThat(
            AnnotationUtils.getFieldAnnotationValue(
                Person.class, "truc", DataTableProperty.class, DataTableProperty::columnName))
        .isEmpty();
  }

  @Test
  void getFieldAnnotationValueNoMatch() {
    assertThat(
            AnnotationUtils.getFieldAnnotationValue(
                Person.class, "role", DataTableProperty.class, DataTableProperty::columnName))
        .isEmpty();
  }

  @Test
  void getQueryPropertyNoRename() {
    var res = AnnotationUtils.getQueryProperty(Person.class, "id");
    assertThat(res).isEqualTo(Optional.empty());
  }

  @Test
  void getQueryPropertyWithRename() {
    var res = AnnotationUtils.getQueryProperty(Person.class, "firstName");
    assertThat(res).isEqualTo(Optional.of("first_name"));
  }

  @Test
  void getQueryPropertyUnknownField() {
    var res = AnnotationUtils.getQueryProperty(Person.class, "test");
    assertThat(res).isEmpty();
  }

  @Test
  void getJavaProperty() {
    var res = AnnotationUtils.getJavaProperty(Person.class, "id");
    assertThat(res).isEqualTo(Optional.of("id"));
  }

  @Test
  void getJavaPropertyWithRename() {
    var res = AnnotationUtils.getJavaProperty(Person.class, "first_name");
    assertThat(res).isEqualTo(Optional.of("firstName"));
  }

  @Test
  void getJavaPropertyUnknownField() {
    var res = AnnotationUtils.getJavaProperty(Person.class, "test");
    assertThat(res).isEmpty();
  }

  @DataTable(value = "select 1")
  private static class Person {

    private Long id;

    @DataTableProperty(columnName = "first_name")
    private String firstName;

    @DataTableIgnore private String role;
  }
}
