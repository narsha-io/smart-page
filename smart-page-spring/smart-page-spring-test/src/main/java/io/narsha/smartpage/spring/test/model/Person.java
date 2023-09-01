package io.narsha.smartpage.spring.test.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Person {

  private Long id;
  private String firstName;
  private String role;
}
