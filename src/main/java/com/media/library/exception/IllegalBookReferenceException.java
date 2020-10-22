package com.media.library.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class IllegalBookReferenceException extends RuntimeException {

    public IllegalBookReferenceException(String reference) {
        super(String.format("[reference: %s] Illegal book reference: it needs to start with BOOK-", reference));
    }

}
