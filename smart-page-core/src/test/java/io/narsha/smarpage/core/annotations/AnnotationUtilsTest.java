package io.narsha.smarpage.core.annotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.narsha.smartpage.core.annotations.AnnotationUtils;
import io.narsha.smartpage.core.annotations.DataTable;
import io.narsha.smartpage.core.annotations.DataTableIgnore;
import io.narsha.smartpage.core.annotations.DataTableProperty;
import io.narsha.smartpage.core.exceptions.InternalException;
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
    assertThat(res).isEqualTo("first_name");
  }

  @Test
  void getFieldAnnotationValueUnknownField() {
    assertThrows(
        InternalException.class,
        () ->
            AnnotationUtils.getFieldAnnotationValue(
                Person.class, "truc", DataTableProperty.class, DataTableProperty::columnName));
  }

  @Test
  void getFieldAnnotationValueNoMatch() {
    var ex =
        assertThrows(
            InternalException.class,
            () ->
                AnnotationUtils.getFieldAnnotationValue(
                    Person.class, "role", DataTableProperty.class, DataTableProperty::columnName));

    assertThat(ex.getMessage()).isEqualTo("Cannot parse the current object");
  }

  @Test
  void getQueryPropertyNoRename() {
    var res = AnnotationUtils.getQueryProperty(Person.class, "id");
    assertThat(res).isEqualTo("id");
  }

  @Test
  void getQueryPropertyWithRename() {
    var res = AnnotationUtils.getQueryProperty(Person.class, "firstName");
    assertThat(res).isEqualTo("first_name");
  }

  @Test
  void getQueryPropertyUnknownField() {
    var res = AnnotationUtils.getQueryProperty(Person.class, "test");
    assertThat(res).isEqualTo("test"); // TODO do I want to keep this ?
  }

  @Test
  void getJavaProperty() {
    var res = AnnotationUtils.getJavaProperty(Person.class, "id");
    assertThat(res).isEqualTo("id");
  }

  @Test
  void getJavaPropertyWithRename() {
    var res = AnnotationUtils.getJavaProperty(Person.class, "first_name");
    assertThat(res).isEqualTo("firstName");
  }

  @Test
  void getJavaPropertyUnknownField() {
    var res = AnnotationUtils.getJavaProperty(Person.class, "test");
    assertThat(res).isEqualTo("test"); // TODO do I want to keep this ?
  }

  @DataTable(value = "select 1")
  public static class Person {

    private Long id;

    @DataTableProperty(columnName = "first_name")
    private String firstName;

    @DataTableIgnore private String role;
  }
}
