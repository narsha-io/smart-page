package io.narsha.smarpage.spring.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.narsha.smartpage.core.utils.HeaderUtils;
import io.narsha.smartpage.web.test.AbstractSmartPageWebTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractSmartPageWebSpringTest extends AbstractSmartPageWebTest {

  @Autowired private MockMvc mockMvc;

  @MethodSource("webSource")
  @ParameterizedTest(name = "{index} -> {0}")
  void checkHttp(String name, String url, String linkHeader, String totalCount, String content)
      throws Exception {
    this.mockMvc
        .perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(header().string(HeaderUtils.LINK_HEADER, linkHeader))
        .andExpect(header().string(HeaderUtils.X_TOTAL_COUNT, totalCount))
        .andExpect(content().json(content, true));
  }
}
