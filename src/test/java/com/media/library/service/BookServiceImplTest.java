package com.media.library.service;

import com.media.library.exception.BookNotFoundException;
import com.media.library.exception.IllegalBookReferenceException;
import com.media.library.model.Book;
import com.media.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.media.library.model.BookGenerator.createBook;
import static java.time.ZonedDateTime.now;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Calling BookService")
class BookServiceImplTest {

    private BookRepository repository = mock(BookRepository.class);

    private BookServiceImpl service = new BookServiceImpl(repository);

    @BeforeEach
    void setup(){
        reset(repository);
    }

    @Nested
    @DisplayName("given an invalid book reference")
    class GivenAnInvalidBookReference {

        @Test
        @DisplayName("when calling retrieveBook function, it should throw an exception")
        void whenBookReferenceIsInvalidItShouldThrowAnException() {

            // GIVEN
            var reference = "INVALID-TEXT";

            // WHEN
            var exception = assertThrows(IllegalBookReferenceException.class, ()-> service.retrieveBook(reference));

            // THEN
            assertThat(exception.getMessage()).isEqualTo("[reference: INVALID-TEXT] Illegal book reference: it needs to start with BOOK-");
            verifyNoInteractions(repository);

        }

        @Nested
        @DisplayName("given a valid book reference")
        class GivenAValidBookReference {

            @Test
            @DisplayName("when retrieveBook function is called and no book is found, it should throw an exception")
            void whenBookReferenceIsValidButNoBookWasFoundtShouldThrowAnException() {

                // GIVEN
                var reference = "BOOK-MISSINGBOOK";
                when(repository.findByReference(reference)).thenReturn(empty());

                // WHEN
                var exception = assertThrows(BookNotFoundException.class, ()-> service.retrieveBook(reference));

                // THEN
                assertThat(exception.getMessage()).isEqualTo("[reference: BOOK-MISSINGBOOK] Book not found");

            }

            @Test
            @DisplayName("when retrieveBook function is called and a book is found, it should return it")
            void whenBookReferenceIsInvalidItShouldThrowAnException() {

                // GIVEN
                var reference = "BOOK-THEOLDM4N";
                var book = createBook(reference);
                when(repository.findByReference(reference)).thenReturn(of(book));

                // WHEN
                var returnedBook = service.retrieveBook(reference);

                // THEN
                assertThat(returnedBook).isEqualTo(book);
                verify(repository, times(1)).findByReference(reference);
                verifyNoMoreInteractions(repository);

            }

        }



    }


}