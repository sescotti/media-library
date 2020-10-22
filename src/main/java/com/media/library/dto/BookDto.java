package com.media.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class BookDto {

    private Long id;
    private ZonedDateTime created;
    private ZonedDateTime lastUpdated;
    private String reference;
    private String title;
    private String review;

}
