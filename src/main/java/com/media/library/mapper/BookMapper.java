package com.media.library.mapper;

import com.media.library.dto.BookDto;
import com.media.library.dto.BookSummaryDto;
import com.media.library.model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public BookDto toDto(Book book){
        return new BookDto(
            book.getId(),
            book.getCreated(),
            book.getLastUpdated(),
            book.getReference(),
            book.getTitle(),
            book.getReview()
        );
    }

    public BookSummaryDto toSummaryDto(String reference, String summary){
        return new BookSummaryDto(reference, summary);
    }

}
