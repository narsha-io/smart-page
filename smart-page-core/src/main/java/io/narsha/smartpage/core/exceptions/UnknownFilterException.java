package io.narsha.smartpage.core.exceptions;

public class UnknownFilterException extends Exception {

    private final String type;

    public UnknownFilterException(String type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return String.format("Unrecognized filter type : %s", this.type);
    }
}
