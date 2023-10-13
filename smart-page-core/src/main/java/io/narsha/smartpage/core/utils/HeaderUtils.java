package io.narsha.smartpage.core.utils;

import io.narsha.smartpage.core.SmartPageResult;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderUtils {

  public static final String LINK_HEADER = "Link";
  public static final String X_TOTAL_COUNT = "X-Total-Count";
  private static final Pattern PAGE_PATTERN = Pattern.compile("(&|\\?)page=\\d+");

  public static <T> String generateHeader(
      String baseUrl, Integer page, Integer size, SmartPageResult<T> result) {
    final var link = new StringBuilder();
    if (page + 1 < result.total() / size) { // check here
      link.append("<").append(buildURI(baseUrl, page + 1)).append(">; rel=\"next\",");
    }
    if (page > 1) {
      link.append("<").append(buildURI(baseUrl, page - 1)).append(">; rel=\"prev\",");
    }

    var lastPage = 0;
    if (result.total() / size > 0) {
      lastPage = (result.total() / size) - 1;
    }

    link.append("<").append(buildURI(baseUrl, lastPage)).append(">; rel=\"prev\",");
    link.append("<").append(buildURI(baseUrl, 0)).append(">; rel=\"first\",");
    return link.toString();
  }

  public static String buildURI(String baseUrl, Integer page) {
    final var delimiter = "?&=";
    final var tokenizer = new StringTokenizer(baseUrl, delimiter, true);
    final var builder = new StringBuilder();

    var lastToken = "";

    while (tokenizer.hasMoreTokens()) {
      var token = tokenizer.nextToken();
      if (!delimiter.contains(token)) {
        if (lastToken.equals("page")) {
          builder.append(page);
          lastToken = "";
          continue;
        }
        lastToken = token;
      }
      builder.append(token);
    }

    var res = builder.toString();

    if (!PAGE_PATTERN.matcher(res).find()) {
      res += (res.contains("?") ? "&" : "?") + "page=" + page;
    }
    return res;
  }
}
