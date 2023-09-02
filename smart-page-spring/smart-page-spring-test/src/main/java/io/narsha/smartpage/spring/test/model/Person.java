package io.narsha.smartpage.spring.test.model;

import io.narsha.smartpage.core.annotations.DataTable;
import io.narsha.smartpage.core.annotations.DataTableIgnore;
import io.narsha.smartpage.core.annotations.DataTableProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@DataTable(value = "select id, first_name, role from person")
public class Person {

  private Long id;

  @DataTableProperty(columnName = "first_name")
  private String firstName;

  @DataTableIgnore private String role;
}
