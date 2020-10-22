package com.media.library.model;

import static java.time.ZonedDateTime.now;

public class BookGenerator {

    public static Book createBook(String bookReference){
        return new Book(
            1L,
            now(),
            now(),
            bookReference,
            "The Old Man and the Sea",
            "The Old Man and the Sea is a short novel written by the American author Ernest Hemingway in 1951 in Cayo Blanco (Cuba), and published in 1952.[1] It was the last major work of fiction written by Hemingway that was published during his lifetime. One of his most famous works, it tells the story of Santiago, an aging Cuban fisherman who struggles with a giant marlin far out in the Gulf Stream off the coast of Cuba."
        );
    }
}
