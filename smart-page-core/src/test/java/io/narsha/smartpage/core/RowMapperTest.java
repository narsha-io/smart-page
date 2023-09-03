package io.narsha.smartpage.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.Setter;
import org.junit.jupiter.api.Test;

class RowMapperTest {

  private static final RowMapper rowMapper = new RowMapper(new ObjectMapper());

  @Test
  void emptyMap() {
    final var res = rowMapper.convert(Map.of(), Car.class);
    assertThat(res).isNotNull();
    assertThat(res.id).isNull();
    assertThat(res.name).isNull();
  }

  @Test
  void validMap() {
    final var res = rowMapper.convert(Map.of("id", "1", "name", "206"), Car.class);
    assertThat(res).isNotNull();
    assertThat(res.id).isEqualTo(1L);
    assertThat(res.name).isEqualTo("206");
  }

  @Setter
  public static class Car {
    private Long id;
    private String name;
  }
}
