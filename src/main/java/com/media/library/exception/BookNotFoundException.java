package com.media.library.exception;

/*
 * Copyright © 2015 Sky plc All Rights reserved.
 * Please do not make your solution publicly available in any way e.g. post in forums or commit to GitHub.
 */

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String reference) {
        super(String.format("[reference: %s] Book not found", reference));
    }

}
