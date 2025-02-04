package ca.sheridan.alzuhait.assignment1.services;

import ca.sheridan.alzuhait.assignment1.beans.Book;
import ca.sheridan.alzuhait.assignment1.beans.BookList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class BookListService {

    @Autowired
    private BookList bookList; // Injection of BookList

    private Random random = new Random(); // Instance of Random for random generation

    // Method to initialize default books in the BookList
    public void initialize() {
        List<Book> books = new ArrayList<>();

        // Create 10 default books with random prices
        for (long i = 1; i <= 10; i++) {
            Book book = Book.builder()
                    .bookISBN(generateUniqueISBN(books))
                    .bookTitle("Random Title: " + i)
                    .bookAuthor("Random Author: " + i)
                    .bookPrice(generateRandomPrice())
                    .build();
            books.add(book);
        }

        // Set the initialized list of books in the BookList bean
        bookList.setBooks(books);
    }


    private double generateRandomPrice() {
        double price = (1000 + random.nextInt(9000)) / 100.0;
        BigDecimal bd = new BigDecimal(price);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Generate a random 6-digit ISBN that is unique within the provided book list
    public String generateUniqueISBN(List<Book> books) {
        Set<String> existingISBNs = new HashSet<>();

        // Collect existing ISBNs from the current book list
        for (Book book : books) {
            existingISBNs.add(book.getBookISBN());
        }

        String newISBN;

        // Keep generating a new ISBN until it is unique
        do {
            newISBN = String.valueOf(100000 + random.nextInt(900000));  // 6-digit random ISBN
        } while (existingISBNs.contains(newISBN));

        return newISBN;
    }
}
