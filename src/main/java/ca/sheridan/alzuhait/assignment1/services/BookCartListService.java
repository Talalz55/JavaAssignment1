package ca.sheridan.alzuhait.assignment1.services;

import ca.sheridan.alzuhait.assignment1.beans.BookCartList;
import ca.sheridan.alzuhait.assignment1.beans.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BookCartListService {

    @Autowired
    private BookCartList bookCartList;

    public final double taxRate = 0.13;

    public void initialize(){
        bookCartList.setBooks(new ArrayList<>());
    }

    // Helper method to round a double to 2 decimal places
    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    // Calculate the subtotal (sum of all book prices)
    public double getSubTotal() {
        double subTotal = bookCartList
                .getBooks()
                .stream()
                .mapToDouble(Book::getBookPrice)
                .sum();
        return roundToTwoDecimals(subTotal);
    }

    // Calculate the tax amount (subtotal * taxRate)
    public double getTaxAmount(){
        double taxAmount = getSubTotal() * taxRate;
        return roundToTwoDecimals(taxAmount);
    }

    // Calculate the total (subtotal + tax amount)
    public double getTotal(){
        double total = getSubTotal() + getTaxAmount();
        return roundToTwoDecimals(total);
    }
}
