package io.narsha.smartpage.spring.test.validator;

import static org.assertj.core.api.Assertions.assertThat;

import io.narsha.smartpage.spring.test.model.Person;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonValidator {

  public static void containsIds(List<Person> persons, Set<Long> ids) {
    assertThat(persons).isNotNull();
    assertThat(ids).isNotNull();
    final var personsId = persons.stream().map(Person::getId).collect(Collectors.toSet());
    assertThat(personsId).hasSameSizeAs(ids); // check duplicate
    assertThat(personsId).containsAll(ids);
  }

  public static void validate(List<Person> persons) {
    assertThat(persons).isNotNull();
    persons.forEach(
        p -> {
          assertThat(p).isNotNull();
          if (p.getId().equals(1L)) {
            validateArthur(p);
          } else if (p.getId().equals(2L)) {
            validateLeodagan(p);
          } else if (p.getId().equals(3L)) {
            validatePerceval(p);
          } else if (p.getId().equals(4L)) {
            validateKaradoc(p);
          } else {
            validateKadoc(p);
          }
        });
  }

  public static void validateArthur(Person person) {
    validate(person, 1L, "Arthur", "Roi du royaume de Logres");
  }

  public static void validateLeodagan(Person person) {
    validate(person, 2L, "Leodagan", "Roi de Carmélide");
  }

  public static void validatePerceval(Person person) {
    validate(person, 3L, "Perceval", "Venu des etoiles");
  }

  public static void validateKaradoc(Person person) {
    validate(person, 4L, "Karadoc", "Le croque monsieur");
  }

  public static void validateKadoc(Person person) {
    validate(person, 5L, "Kadoc", "Elle est où la poulette ?");
  }

  private static void validate(Person person, Long id, String firstName, String role) {
    assertThat(person).isNotNull();
    assertThat(person.getId()).isEqualTo(id);
    assertThat(person.getFirstName()).isEqualTo(firstName);
    assertThat(person.getRole()).isEqualTo(role);
  }
}
