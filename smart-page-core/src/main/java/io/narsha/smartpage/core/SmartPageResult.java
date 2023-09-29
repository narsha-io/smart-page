package io.narsha.smartpage.core;

import java.util.List;

public record SmartPageResult<T>(List<T> result, Long totalResult) {}
