package io.narsha.smartpage.core.utils;

import io.narsha.smartpage.core.SmartPageResult;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Header generator following http header RFC988 more information
 * https://datatracker.ietf.org/doc/html/rfc5988#page-6
 */
public class HeaderUtils {

  /** default constructor */
  private HeaderUtils() {}

  /** Http header Link */
  public static final String LINK_HEADER = "Link";

  /** Http header X total count */
  public static final String X_TOTAL_COUNT = "X-Total-Count";

  private static final Pattern PAGE_PATTERN = Pattern.compile("[&?]page=\\d+");

  /**
   * Generate link part header
   *
   * @param baseUrl current sql query
   * @param page the target page
   * @param size the page size
   * @param result the query result used to determined how many pages exist
   * @param <T> DTO type
   * @return the Link part
   */
  public static <T> String generateHeader(
      String baseUrl, Integer page, Integer size, SmartPageResult<T> result) {
    final var link = new StringBuilder();

    firstPage(page).ifPresent(value -> generateLink(link, baseUrl, "first", value));
    previousPage(page).ifPresent(value -> generateLink(link, baseUrl, "prev", value));
    nextPage(page, size, result.total())
        .ifPresent(value -> generateLink(link, baseUrl, "next", value));
    lastPage(page, size, result.total())
        .ifPresent(value -> generateLink(link, baseUrl, "last", value));

    var res = link.toString().trim();
    if (res.endsWith(",")) {
      res = res.substring(0, res.lastIndexOf(","));
    }
    return res;
  }

  private static void generateLink(
      StringBuilder builder, String baseUrl, String name, Integer page) {
    builder
        .append("<")
        .append(buildURI(baseUrl, page))
        .append(">; rel=\"")
        .append(name)
        .append("\",");
  }

  /**
   * Replace a page attribute in an http query
   *
   * @param baseUrl the current http query
   * @param page the target page
   * @return the new http query
   */
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

    if (!PAGE_PATTERN.matcher(res).find() && !page.equals(0)) {
      res += (res.contains("?") ? "&" : "?") + "page=" + page;
    }
    return res;
  }

  static Optional<Integer> firstPage(Integer currentPage) {
    return Optional.ofNullable(currentPage >= 1 ? 0 : null);
  }

  static Optional<Integer> lastPage(Integer currentPage, Integer pageSize, Integer totalElement) {
    final var lastPage = maxPage(pageSize, totalElement);
    return Optional.ofNullable(lastPage.equals(currentPage) ? null : lastPage);
  }

  static Optional<Integer> nextPage(Integer currentPage, Integer pageSize, Integer totalElement) {
    return Optional.ofNullable(
        maxPage(pageSize, totalElement).equals(currentPage) ? null : currentPage + 1);
  }

  static Optional<Integer> previousPage(Integer currentPage) {
    return Optional.ofNullable(currentPage > 0 ? currentPage - 1 : null);
  }

  static Integer maxPage(Integer pageSize, Integer totalElement) {
    final var total = (1.0 * totalElement / pageSize);
    final double res = total % 1 == 0 ? total - 1 : total;
    return (int) res;
  }
}
