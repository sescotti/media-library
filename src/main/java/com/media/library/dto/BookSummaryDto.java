package com.media.library.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookSummaryDto {

    @NonNull
    private final String reference;
    @NonNull
    private final String summary;

}
