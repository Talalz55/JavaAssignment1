package ca.sheridan.alzuhait.assignment1.controllers;

import ca.sheridan.alzuhait.assignment1.beans.Book;
import ca.sheridan.alzuhait.assignment1.beans.BookList;
import ca.sheridan.alzuhait.assignment1.beans.BookCartList;
import ca.sheridan.alzuhait.assignment1.services.BookCartListService;
import ca.sheridan.alzuhait.assignment1.services.BookListService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@Slf4j  // Lombok logger
public class BookStoreController {

    @Autowired
    private BookList bookList;

    @Autowired
    private BookCartList bookCartList;

    @Autowired
    private BookListService bookListService;

    @Autowired
    private BookCartListService bookCartListService;
    @Autowired
    private Book book;

    // Initialize the list beans
    @PostConstruct
    public void init() {
        log.info("Initializing book list and book cart list");
        bookListService.initialize(); // Initialize the books
        bookCartListService.initialize(); // Initialize the books
        log.info("Initialized successfully.");
    }

    // Display the homepage
    @GetMapping("/")
    public String getIndex() {
        log.debug("Accessed the homepage.");
        return "index";
    }

    // Display the list of available books
    @GetMapping("/AvailableBooks")
    public String getAvailableBooks(Model model) {
        log.debug("Accessed the available books page.");
        List<Book> books = bookList.getBooks();
        model.addAttribute("books", books);
        log.info("Available books list displayed with {} books.", books.size());
        return "AvailableBooks";
    }

    // Handle the POST request to add a new book
    @PostMapping("/AddBook")
    public String postAddBook(
            Model model,
            @RequestParam("bookTitle") String bookTitle,
            @RequestParam("bookAuthor") String bookAuthor,
            @RequestParam("bookPrice") Double bookPrice
    ) {
        log.info("Adding new book: {} by {} with price {}", bookTitle, bookAuthor, bookPrice);

        List<Book> books = bookList.getBooks();
        String isbn = bookListService.generateUniqueISBN(books); // Generate unique ISBN from service

        Book newBook = Book.builder()
                .bookISBN(isbn)
                .bookTitle(bookTitle)
                .bookAuthor(bookAuthor)
                .bookPrice(bookPrice)
                .build();

        books.add(newBook); // Add new book to the list
        bookList.setBooks(books); // Update the book list
        model.addAttribute("books", books); // Add the updated book list to the model

        log.info("New book added successfully: {}", newBook);
        return "AvailableBooks"; // Return the updated book list view
    }

    // Display books available for shopping and cart count
    @GetMapping("/ShoppingCart")
    public String getShoppingCart(Model model) {
        log.debug("Accessed the shopping books page.");
        List<Book> books = bookList.getBooks();
        List<Book> cartBooks = bookCartList.getBooks();
        int bookCartCount = cartBooks.size();

        model.addAttribute("books", books);  // Add available books
        model.addAttribute("bookCartCount", bookCartCount);  // Add cart count

        log.info("Displayed shopping cart with {} available books and {} in cart.", books.size(), cartBooks.size());
        return "ShoppingCart"; // Return the shopping books view
    }

    // Add a book to the cart
    @GetMapping("/AddBookToCart/{bookISBN}")
    public String getAddBookToCart(
            Model model,
            @PathVariable String bookISBN
    ) {
        log.info("Attempting to add book with ISBN {} to the cart.", bookISBN);

        Book bookToAdd = bookList
                .getBooks()
                .stream()
                .filter(book -> book.getBookISBN().equals(bookISBN))  // Find the book by ISBN
                .findFirst()
                .orElse(null);


        if (bookToAdd != null) {
            System.out.println(bookToAdd);
            List<Book> newBooks = bookCartList.getBooks();
            newBooks.add(bookToAdd);
            bookCartList.setBooks(newBooks);

            log.info("Book with ISBN {} added to the cart.", bookISBN);
        } else {
            log.warn("Book with ISBN {} not found, cannot add to cart.", bookISBN);
        }
        return this.getShoppingCart(model);
    }

    // Display the checkout page with cart details, subtotal, tax, and total
    @GetMapping("/CheckOut")
    public String getCheckOut(Model model) {
        log.debug("Accessed the checkout page.");
        model.addAttribute("books", bookCartList.getBooks());
        model.addAttribute("subTotal", bookCartListService.getSubTotal());
        model.addAttribute("taxAmount", bookCartListService.getTaxAmount());
        model.addAttribute("total", bookCartListService.getTotal());

        log.info("Displayed checkout page with total amount: {}", bookCartListService.getTotal());
        return "CheckOut";  // Return checkout view
    }
}
