package com.media.library.controller;

import com.media.library.dto.BookDto;
import com.media.library.mapper.BookMapper;
import com.media.library.model.Book;
import com.media.library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;
    private final BookMapper mapper;

    public BookController(BookService service, BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{bookReference}")
    public @ResponseBody ResponseEntity<BookDto> findBookByReference(@PathVariable String bookReference){

        Book book = service.retrieveBook(bookReference);
        BookDto dto = mapper.toDto(book);

        return ok(dto);

    }

}
