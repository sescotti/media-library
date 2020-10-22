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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookSummaryController.class)
@ExtendWith(SpringExtension.class)
@Import({BookMapper.class})
class BookSummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService service;

    @Test
    @DisplayName("when retrieving a book summary by reference it should return the reference and its summary")
    public void retrieveByBookReference() throws Exception {

        // GIVEN
        var bookReference = "BOOK-REFERENCE";
        var summary = "[BOOK-WILL987] The Wind In The Willows - With the arrival of spring and fine weather outside...";

        when(service.getBookSummary(bookReference)).thenReturn(summary);

        // THEN
        mockMvc
                .perform(get("/book_summaries/{0}", bookReference))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference").value(bookReference))
                .andExpect(jsonPath("$.summary").value(summary));

    }


    @Test
    @DisplayName("when no book is found with given reference it should return a 404")
    public void retrieveByBookReferenceWhenNotFound() throws Exception {

        // GIVEN
        var bookReference = "BOOK-REFERENCE";

        when(service.getBookSummary(bookReference)).thenThrow(new BookNotFoundException(bookReference));

        // THEN
        mockMvc
                .perform(get("/book_summaries/{0}", bookReference))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("when no book reference is invalid it should return a 400")
    public void retrieveBookWithAnInvalidReference() throws Exception {

        // GIVEN
        var bookReference = "INVALID-TEXT";

        when(service.getBookSummary(bookReference)).thenThrow(new IllegalBookReferenceException(bookReference));

        // THEN
        mockMvc
                .perform(get("/book_summaries/{0}", bookReference))
                .andExpect(status().isBadRequest());

    }

}