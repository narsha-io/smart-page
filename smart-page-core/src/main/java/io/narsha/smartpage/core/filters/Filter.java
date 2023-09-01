package io.narsha.smartpage.core.filters;

import java.util.function.Supplier;

public enum Filter {

    EQUALS(EqualsFilter::new);

    final Supplier<? extends FilterParser> parser;

    Filter(Supplier<? extends FilterParser> parser) {
        this.parser = parser;
    }

    public Supplier<? extends FilterParser> getParser() {
        return this.parser;
    }
}
