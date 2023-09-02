package io.narsha.smarpage.core.filters;

import static org.assertj.core.api.Assertions.assertThat;

import io.narsha.smartpage.core.filters.FilterRegistrationService;
import io.narsha.smartpage.core.filters.InFilterFactory;
import org.junit.jupiter.api.Test;

class FilterRegistrationServiceTest {

  @Test
  void validTest() {
    final var service = new FilterRegistrationService();
    service.register(new InFilterFactory());
    var res = service.get(String.class, "in");
    assertThat(res).isPresent();
  }

  @Test
  void invalidTest() {
    final var service = new FilterRegistrationService();
    service.register(new InFilterFactory());
    var res = service.get(String.class, "equals");
    assertThat(res).isEmpty();
  }
}
