package io.narsha.smartpage.core;

import java.util.List;

public record SmartPageResult<T>(List<T> data, Integer total) {}
