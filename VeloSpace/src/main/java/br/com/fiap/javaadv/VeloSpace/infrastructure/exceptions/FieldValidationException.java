package br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions;

public class FieldValidationException extends RuntimeException {

    private final String field;
    private final String message;

    public FieldValidationException(String field, String message) {
        super(message);

        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
