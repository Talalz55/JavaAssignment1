package ca.sheridan.alzuhait.assignment1;

import ca.sheridan.alzuhait.assignment1.beans.Book;
import ca.sheridan.alzuhait.assignment1.beans.BookList;
import ca.sheridan.alzuhait.assignment1.services.BookListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Assignment1ApplicationTests {

    @Mock
    private BookList bookList;

    @InjectMocks
    private BookListService bookListService;

    @Test
    void testGenerateUniqueISBN() {
        List<Book> books = new ArrayList<>();
        books.add(Book.builder().bookISBN("123456").build());
        books.add(Book.builder().bookISBN("654321").build());

        String newISBN = bookListService.generateUniqueISBN(books);

        assertNotNull(newISBN);
        assertFalse(newISBN.equals("123456") || newISBN.equals("654321"));
        assertTrue(newISBN.matches("\\d{6}"));
    }

    @Test
    void testInitialize() {
        when(bookList.getBooks()).thenReturn(new ArrayList<>());

        bookListService.initialize();

        verify(bookList, times(1)).setBooks(anyList());
        assertEquals(10, bookList.getBooks().size());
        assertTrue(bookList.getBooks().stream().allMatch(book -> book.getBookISBN() != null));
    }
}
