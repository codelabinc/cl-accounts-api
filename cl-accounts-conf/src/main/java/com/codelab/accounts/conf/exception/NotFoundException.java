package com.codelab.accounts.conf.exception;

/**
 * @author lordUhuru 04/12/2019
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
