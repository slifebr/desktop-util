package edu.porgamdor.util.desktop.ss.util;

public class ValidacaoException extends Exception {
    public ValidacaoException(Throwable throwable) {
        super(throwable);
    }

    public ValidacaoException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public ValidacaoException(String string) {
        super(string);
    }

    public ValidacaoException() {
        super();
    }
}
