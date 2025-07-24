package com.library.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.BookTransaction;
import com.library.service.BookTransactionService;

@RestController
@RequestMapping("/transaction")
public class BookTransactionController {
	
	@Autowired
    private BookTransactionService transactionService;
    
    @PostMapping("/borrow/{username}/{bookId}")
    public ResponseEntity<BookTransaction> borrowBook(@PathVariable String username, @PathVariable int bookId) {
        try {
            BookTransaction transaction = transactionService.borrowBook(username, bookId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
        	System.out.println("Borrow Failed: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/return/{transactionId}")
    public ResponseEntity<BookTransaction> returnBook(@PathVariable int transactionId) {
        try {
            BookTransaction transaction = transactionService.returnBook(transactionId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/extend/{username}/{transactionId}/{days}")
    public String extendDates(@PathVariable String username,@PathVariable int transactionId,@PathVariable int days) {
    		return transactionService.extendDueDate(username,transactionId, days);
    }
    @GetMapping("/getall")
    public List<BookTransaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
    
    @GetMapping("/user/{username}")
    public List<BookTransaction> getUserTransactions(@PathVariable String username) {
        return transactionService.getUserTransactions(username);
    }
    
    @GetMapping("/user/{username}/current")
    public List<BookTransaction> getUserCurrentBorrowedBooks(@PathVariable String username) {
        return transactionService.getUserCurrentBorrowedBooks(username);
    }
    
    @GetMapping("/book/{bookId}")
    public List<BookTransaction> getBookTransactions(@PathVariable int bookId) {
        return transactionService.getBookTransactions(bookId);
    }
    
    @GetMapping("/current-borrowed")
    public List<BookTransaction> getCurrentBorrowedBooks() {
        return transactionService.getCurrentBorrowedBooks();
    }
    
    @GetMapping("/overdue")
    public List<BookTransaction> getOverdueBooks() {
        return transactionService.getOverdueBooks();
    }
    
}
