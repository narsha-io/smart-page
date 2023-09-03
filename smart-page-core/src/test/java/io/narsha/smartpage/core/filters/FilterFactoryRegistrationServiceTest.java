package io.narsha.smartpage.core.filters;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FilterFactoryRegistrationServiceTest {

  @Test
  void validTest() {
    final var service = new FilterFactoryRegistrationService();
    service.register(new InFilterFactory());
    var res = service.get(String.class, "in");
    assertThat(res).isPresent();
  }

  @Test
  void invalidTest() {
    final var service = new FilterFactoryRegistrationService();
    service.register(new InFilterFactory());
    var res = service.get(String.class, "equals");
    assertThat(res).isEmpty();
  }
}
