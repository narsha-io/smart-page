package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/** Basic implementation of contains parser */
@Getter
public class ContainsFilter extends FilterParser<String, String> {

  /** the parsed value */
  private String value;

  /** Contains is only applicable on String property */
  public ContainsFilter() {
    super(String.class);
  }

  /**
   * Parse the parameter value to have a unique string
   *
   * @param objectMapper mapper to convert String as a targetClass (which is String here)
   * @param value the value we want to parse
   */
  @Override
  public void parse(ObjectMapper objectMapper, String[] value) {
    var parsed =
        Stream.of(value)
            .filter(Objects::nonNull)
            .filter(StringUtils::isNotEmpty)
            .collect(Collectors.joining(","));
    if (StringUtils.isEmpty(parsed)) {
      parsed = "";
    }
    this.value = objectMapper.convertValue(parsed, targetClass);
  }
}
