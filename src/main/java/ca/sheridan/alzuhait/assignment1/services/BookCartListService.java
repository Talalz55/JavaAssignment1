package ca.sheridan.alzuhait.assignment1.services;

import ca.sheridan.alzuhait.assignment1.beans.BookCartList;
import ca.sheridan.alzuhait.assignment1.beans.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookCartListService {

    @Autowired
    private BookCartList bookCartList;

    public final double taxRate = 0.13;

    // Calculate the subtotal (sum of all book prices)
    public double getSubTotal() {
        return bookCartList
                .getBooks()
                .stream()
                .mapToDouble(Book::getBookPrice)
                .sum();
    }

    // Calculate the tax amount (subtotal * taxRate)
    public double getTaxAmount(){
        return getSubTotal() * taxRate;
    }

    // Calculate the total (subtotal + tax amount)
    public double getTotal(){
        return getSubTotal() + getTaxAmount();
    }
}
