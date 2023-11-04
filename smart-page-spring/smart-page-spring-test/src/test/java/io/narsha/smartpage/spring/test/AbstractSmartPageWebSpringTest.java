package io.narsha.smartpage.spring.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.narsha.smartpage.core.utils.HeaderUtils;
import io.narsha.smartpage.web.test.AbstractSmartPageWebTest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/** Global Spring stack test class */
@SpringBootTest
@AutoConfigureMockMvc
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractSmartPageWebSpringTest extends AbstractSmartPageWebTest {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Autowired private MockMvc mockMvc;

  @MethodSource("webSource")
  @ParameterizedTest(name = "{index} -> {0}")
  void checkHttp(String name, String url, String linkHeader, Integer totalCount, String content)
      throws Exception {

    var exceptedBody = (ArrayNode) OBJECT_MAPPER.readTree(content);

    var check =
        this.mockMvc
            .perform(get(url))
            .andDo(print())
            .andExpect(!exceptedBody.isEmpty() ? status().isOk() : status().isNoContent())
            .andExpect(
                !exceptedBody.isEmpty() ? content().json(content) : jsonPath("$").doesNotExist());

    if (exceptedBody.size() != totalCount)
      check
          .andExpect(header().string(HeaderUtils.LINK_HEADER, linkHeader))
          .andExpect(header().string(HeaderUtils.X_TOTAL_COUNT, String.valueOf(totalCount)))
          .andExpect(content().json(content, true));
  }
}
