package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class ContainsFilter extends FilterParser<String, String> {

  private String value;

  public ContainsFilter() {
    super(String.class);
  }

  @Override
  public void parse(ObjectMapper objectMapper, String[] value) {
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
}
