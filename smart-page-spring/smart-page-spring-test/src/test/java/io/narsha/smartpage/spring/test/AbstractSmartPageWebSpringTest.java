package io.narsha.smartpage.spring.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.narsha.smartpage.core.utils.HeaderUtils;
import io.narsha.smartpage.web.AbstractSmartPageWebTest;
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

    var result = this.mockMvc.perform(get(url)).andDo(print()).andReturn();

    assertEquals(result.getResponse().getStatus(), exceptedBody.isEmpty() ? 204 : 200);
    var body = new ObjectMapper().readTree(result.getResponse().getContentAsByteArray());
    assertTrue(exceptedBody.isEmpty() ? body.isEmpty() : exceptedBody.equals(body));
    assertEquals(result.getResponse().getHeader(HeaderUtils.X_TOTAL_COUNT), totalCount.toString());

    if (exceptedBody.size() != totalCount) {
      assertEquals(result.getResponse().getHeader(HeaderUtils.LINK_HEADER), linkHeader);
    } else {
      assertFalse(result.getResponse().containsHeader(HeaderUtils.LINK_HEADER));
      assertTrue(linkHeader == null);
    }
  }
}
