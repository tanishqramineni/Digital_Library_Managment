package com.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;

import com.library.entity.BookTransaction;
import com.library.entity.BookTransaction.TransactionStatus;
import com.library.entity.Books;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.BookTransactionRepository;
import com.library.repository.UserRepository;

@Service
public class BookTransactionService {
	@Autowired
	private BookTransactionRepository bookTransactionRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BookRepository bookRepo;
	
	private static final int Default_Borrow_Days=30;
	private static final double Fine_Per_Day=5;
	private static final int Max_Book_Per_User=3;

	//Borrow a Book
	
	public BookTransaction borrowBook(String username,int bookId)
	{
		Optional<User> user=userRepo.findUserName(username);
		Optional<Books> book=bookRepo.findById(bookId);
		//Checking Book Availibility
		
		if(user.isPresent() && book.isPresent())
		{
			User borrowUser=user.get();
			Books booksToBorrow=book.get();
			//Checking Book Availibility
			if(!"ACTIVE".equals(borrowUser.getStatus()))
			{
				throw new RuntimeException("The User is not Activated");
			}
			if(!"AVAILABLE".equals(booksToBorrow.getStatus()))
			{
				throw new RuntimeException("Book is not available for Borrowing...");
			}
			
			//Checking the user with maximum number of Books
			
			long currentBorrowedCount=bookTransactionRepo.countUserBorrowedBooks(borrowUser.getUserId());
			if(currentBorrowedCount>=Max_Book_Per_User)
			{
				throw new RuntimeException("User has reached maximum book borrowing limit");
			}

			BookTransaction transaction=new BookTransaction(booksToBorrow,borrowUser,Default_Borrow_Days);
			booksToBorrow.setStatus("BORROWED");
			bookRepo.save(booksToBorrow);
			return bookTransactionRepo.save(transaction);
		}
		throw new RuntimeException("User or Book not found");
	}
	//Returning a Book:
	public BookTransaction returnBook(int transactionId) {
        Optional<BookTransaction> transaction = bookTransactionRepo.findById(transactionId);
        
        if (transaction.isPresent()) {
            BookTransaction bookTransaction = transaction.get();
            
            if (bookTransaction.getStatus() != TransactionStatus.BORROWED) {
                throw new RuntimeException("Book is not currently borrowed");
            }
            
            // Set return date
            bookTransaction.setReturnDate(LocalDate.now());
            bookTransaction.setStatus(TransactionStatus.RETURNED);
            
            // Calculate fine if overdue
            if (LocalDate.now().isAfter(bookTransaction.getDueDate())) {
                long overdueDays = ChronoUnit.DAYS.between(bookTransaction.getDueDate(), LocalDate.now());
                double fine = overdueDays * Fine_Per_Day;
                bookTransaction.setFineAmount(fine);
            }
            
            // Update book status
            Books book = bookTransaction.getBook();
            book.setStatus("AVAILABLE");
            bookRepo.save(book);
            
            return bookTransactionRepo.save(bookTransaction);
        }
        throw new RuntimeException("Transaction not found");
    }
	public String extendDueDate(String username,int tid,int holidays) {
		Optional<BookTransaction> bt=bookTransactionRepo.findById(tid);
		if(bt.isEmpty()) {
			return "Transaction not found";
		}
		if(!userRepo.getRole(username).equals("LIBRARIAN")) {
			return "User is not authorized";
		}
		BookTransaction b=bt.get();
		LocalDate bd=b.getDueDate().plusDays(holidays);
		b.setDueDate(bd);
		bookTransactionRepo.save(b);
		return "Extended Sucessfully";
	}
	// Get all transactions
    public List<BookTransaction> getAllTransactions() {
        return bookTransactionRepo.findAll();
    }
    
    // Get user's borrowing history
    public List<BookTransaction> getUserTransactions(String username) {
    	Optional<User> u=userRepo.findUserName(username);
    	if(u.isEmpty()) return null;
        return bookTransactionRepo.findByUserUserId(u.get().getUserId());
    }
    
    // Get current borrowed books by user
    public List<BookTransaction> getUserCurrentBorrowedBooks(String username) {
    	Optional<User> u=userRepo.findUserName(username);
    	if(u.isEmpty()) return null;
        return bookTransactionRepo.findUserCurrentBorrowedBooks(u.get().getUserId());
    }
    
    // Get book's transaction history
    public List<BookTransaction> getBookTransactions(int bookId) {
        return bookTransactionRepo.findByBookBid(bookId);
    }
    
    // Get all currently borrowed books
    public List<BookTransaction> getCurrentBorrowedBooks() {
        return bookTransactionRepo.findCurrentBorrowedBooks();
    }
    
    // Get overdue books
    public List<BookTransaction> getOverdueBooks() {
        return bookTransactionRepo.findOverdueBooks(LocalDate.now());
    }
	
}
