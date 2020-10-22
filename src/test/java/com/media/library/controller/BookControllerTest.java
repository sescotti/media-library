package com.media.library.controller;

import com.media.library.exception.BookNotFoundException;
import com.media.library.exception.IllegalBookReferenceException;
import com.media.library.mapper.BookMapper;
import com.media.library.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.media.library.model.BookGenerator.createBook;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@ExtendWith(SpringExtension.class)
@Import({BookMapper.class})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService service;

    @Test
    @DisplayName("when retrieving a book by reference it should return all relevant book info")
    public void retrieveByBookReference() throws Exception {

        // GIVEN
        var bookReference = "BOOK-REFERENCE";
        var book = createBook(bookReference);

        when(service.retrieveBook(bookReference)).thenReturn(book);

        // THEN
        mockMvc
                .perform(get("/books/{0}", bookReference))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.last_updated").exists())
                .andExpect(jsonPath("$.reference").value(book.getReference()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.review").value(book.getReview()));

    }


    @Test
    @DisplayName("when no book is found with given reference it should return a 404")
    public void retrieveByBookReferenceWhenNotFound() throws Exception {

        // GIVEN
        var bookReference = "BOOK-REFERENCE";

        when(service.retrieveBook(bookReference)).thenThrow(new BookNotFoundException(bookReference));

        // THEN
        mockMvc
                .perform(get("/books/{0}", bookReference))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("when no book reference is invalid it should return a 400")
    public void retrieveBookWithAnInvalidReference() throws Exception {

        // GIVEN
        var bookReference = "INVALID-TEXT";

        when(service.retrieveBook(bookReference)).thenThrow(new IllegalBookReferenceException(bookReference));

        // THEN
        mockMvc
                .perform(get("/books/{0}", bookReference))
                .andExpect(status().isBadRequest());

    }

}