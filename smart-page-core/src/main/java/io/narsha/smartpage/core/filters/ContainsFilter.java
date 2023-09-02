package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public class ContainsFilter extends FilterParser {

  private Object value;

  @Override
  public void parse(ObjectMapper objectMapper, Class<?> targetClass, String[] value) {
    var tmp =
        Stream.of(value)
            .filter(Objects::nonNull)
            .filter(StringUtils::isNotEmpty)
            .collect(Collectors.joining(","));
    if (StringUtils.isEmpty(tmp)) {
      tmp = "";
    }
    this.value = objectMapper.convertValue(tmp, targetClass);
  }

  @Override
  public String getSQLFragment(String property) {
    return String.format(" like :%s", property);
  }

  public Object getValue() {
    return "%" + this.value + "%";
  }
}
