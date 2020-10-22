package com.media.library.service;

import com.media.library.exception.BookNotFoundException;
import com.media.library.exception.IllegalBookReferenceException;
import com.media.library.model.Book;
import com.media.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private static final Pattern BOOK_REFERENCE_PATTERN = Pattern.compile("BOOK-.+");

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book retrieveBook(String bookReference) throws BookNotFoundException {

        var reference = sanitiseBookReference(bookReference).orElseThrow(()-> new IllegalBookReferenceException(bookReference));

        return this.bookRepository
                .findByReference(reference) // Force uppercase to avoid search issues
                .orElseThrow(()-> new BookNotFoundException(bookReference));

    }

    @Override
    public String getBookSummary(String bookReference) throws BookNotFoundException {
        var book = retrieveBook(bookReference);

        var sanitisedReview = sanitiseReview(book.getReview());

        return "[" + book.getReference() + "] " +
                book.getTitle() + " - " +
                sanitisedReview;


    }

    private Optional<String> sanitiseBookReference(String bookReference) {
        var matcher = BOOK_REFERENCE_PATTERN.matcher(bookReference);
        var match = matcher.matches() ? matcher.group().toUpperCase() : null;

        return Optional.ofNullable(match);
    }

    private String sanitiseReview(String text) {

        var truncatedText = text.replaceAll(TRUNCATE_PATTERN, "$1");

        return truncatedText.split(" ").length == MAX_NUMBER_OF_WORDS_IN_REVIEW ?
                truncatedText + "..." : truncatedText;
    }

    private static final int MAX_NUMBER_OF_WORDS_IN_REVIEW = 9;
    private static final String TRUNCATE_PATTERN = String.format("^((?:\\W*[\\w|.+]+){%s}).*$", MAX_NUMBER_OF_WORDS_IN_REVIEW);
}
