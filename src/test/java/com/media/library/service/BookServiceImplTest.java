package com.media.library.service;

import com.media.library.exception.BookNotFoundException;
import com.media.library.exception.IllegalBookReferenceException;
import com.media.library.model.Book;
import com.media.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

        @Test
        @DisplayName("when calling getBookSummary function, it should throw an exception")
        void whenBookReferenceIsInvalidGetSummaryMethodShouldThrowAnException() {

            // GIVEN
            var reference = "INVALID-TEXT";

            // WHEN
            var exception = assertThrows(IllegalBookReferenceException.class, ()-> service.getBookSummary(reference));

            // THEN
            assertThat(exception.getMessage()).isEqualTo("[reference: INVALID-TEXT] Illegal book reference: it needs to start with BOOK-");
            verifyNoInteractions(repository);

        }


    }

    @Nested
    @DisplayName("given a valid book reference")
    class GivenAValidBookReference {

        @Test
        @DisplayName("when retrieveBook function is called and no book is found, it should throw an exception")
        void whenBookReferenceIsValidButNoBookWasFoundItShouldThrowAnException() {

            // GIVEN
            var reference = "BOOK-MISSINGBOOK";
            when(repository.findByReference(reference)).thenReturn(empty());

            // WHEN
            var exception = assertThrows(BookNotFoundException.class, ()-> service.retrieveBook(reference));

            // THEN
            assertThat(exception.getMessage()).isEqualTo("[reference: BOOK-MISSINGBOOK] Book not found");

        }

        @Test
        @DisplayName("when getBookSummary function is called and no book is found, it should throw an exception")
        void whenBookReferenceIsValidButNoBookWasFoundGetBookSummaryShouldThrowAnException() {

            // GIVEN
            var reference = "BOOK-MISSINGBOOK";
            when(repository.findByReference(reference)).thenReturn(empty());

            // WHEN
            var exception = assertThrows(BookNotFoundException.class, ()-> service.getBookSummary(reference));

            // THEN
            assertThat(exception.getMessage()).isEqualTo("[reference: BOOK-MISSINGBOOK] Book not found");

        }

        @Test
        @DisplayName("when retrieveBook function is called and a book is found, it should return it")
        void whenBookIsFoundRetrieveBookShouldReturnIt() {

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

        @ParameterizedTest
        @DisplayName("when getBookSummary function is called and a book is found, it should return it and truncate it if it's more than 9 words long")
        @MethodSource("com.media.library.service.BookServiceImplTest#booksWithLongReviews")
        void whenBookIsFoundGetBookSummaryShouldReturnItAndTruncateItProperly(Book book, String expectedSummary) {

            // GIVEN
            var reference = book.getReference();
            when(repository.findByReference(book.getReference())).thenReturn(of(book));

            // WHEN
            var summary = service.getBookSummary(reference);

            // THEN
            assertThat(summary).isEqualTo(expectedSummary);
            verify(repository, times(1)).findByReference(reference);
            verifyNoMoreInteractions(repository);

        }

    }

    public static Stream<Arguments> booksWithLongReviews(){

        return Stream.of(
                Arguments.of(createBook("BOOK-THEOLDM4N",
                        "The Old Man and the Sea",
                        "The Old Man and the Sea is a short novel written by the American author Ernest Hemingway in 1951 in Cayo Blanco (Cuba), and published in 1952.[1] It was the last major work of fiction written by Hemingway that was published during his lifetime. One of his most famous works, it tells the story of Santiago, an aging Cuban fisherman who struggles with a giant marlin far out in the Gulf Stream off the coast of Cuba."
                ), "[BOOK-THEOLDM4N] The Old Man and the Sea - The Old Man and the Sea is a short..."),

                Arguments.of(createBook("BOOK-GRUFF472",
                        "The Gruffalo",
                        "A mouse taking a walk in the woods."
                ), "[BOOK-GRUFF472] The Gruffalo - A mouse taking a walk in the woods."),

                Arguments.of(createBook("BOOK-POOH222",
                        "Winnie The Pooh",
                        "In this first volume, we meet all the friends from the Hundred Acre Wood."
                ), "[BOOK-POOH222] Winnie The Pooh - In this first volume, we meet all the friends..."),

                Arguments.of(createBook("BOOK-WILL987",
                        "The Wind In The Willows",
                        "With the arrival of spring and fine weather outside, the good-natured Mole loses patience with spring cleaning. He flees his underground home, emerging to take in the air and ends up at the river, which he has never seen before. Here he meets Rat (a water vole), who at this time of year spends all his days in, on and close by the river. Rat takes Mole for a ride in his rowing boat. They get along well and spend many more days boating, with Rat teaching Mole the ways of the river."
                ), "[BOOK-WILL987] The Wind In The Willows - With the arrival of spring and fine weather outside...")

        );
    }


}