package com.media.library.controller;

import com.media.library.dto.BookSummaryDto;
import com.media.library.mapper.BookMapper;
import com.media.library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/book_summaries")
public class BookSummaryController {

    private final BookService service;
    private final BookMapper mapper;

    public BookSummaryController(BookService service, BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{bookReference}")
    public @ResponseBody
    ResponseEntity<BookSummaryDto> findBookSummaryByReference(@PathVariable String bookReference){

        var summary = service.getBookSummary(bookReference);
        var dto = mapper.toSummaryDto(bookReference, summary);

        return ok(dto);

    }

}
