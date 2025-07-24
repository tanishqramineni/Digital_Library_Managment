package com.library.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.Books;
import com.library.service.BookService;

@RestController
@RequestMapping("/book")
public class BookController {
	@Autowired
	private BookService bookService;

	@PostMapping("/add")
	public ResponseEntity<Books> addBook(@RequestBody Books b) {
		try {
			Books savedBook = bookService.saveBook(b);
			return ResponseEntity.ok(savedBook);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/getall")
	public List<Books> getAll() {
		return bookService.getAll();
	}

	@GetMapping("/getone/{id}")
	public ResponseEntity<Books> getOne(@PathVariable int id) {
        Optional<Books> book = bookService.getOne(id);
        return book.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

	@PutMapping("/update/{bid}/{username}")
	public ResponseEntity<Books> updateRecord(@RequestBody Books b, @PathVariable int bid,@PathVariable String username) {
		Books updatedBook = bookService.updateBook(b, bid,username);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        }
        return ResponseEntity.notFound().build();

	}

	@DeleteMapping("/delete/{bid}/{username}")
    public ResponseEntity<String> deleteBook(@PathVariable int bid,@PathVariable String username) {
        try {
            bookService.deleteBook(bid,username);
            return ResponseEntity.ok("Book deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting book");
        }
    }
	
	@GetMapping("/search/name/{bookName}")
    public List<Books> searchBooksByName(@PathVariable String bookName) {
        return bookService.searchBooksByName(bookName);
    }
    
    @GetMapping("/search/author/{author}")
    public List<Books> searchBooksByAuthor(@PathVariable String author) {
        return bookService.searchBooksByAuthor(author);
    }
    
    @GetMapping("/search/category/{category}")
    public List<Books> searchBooksByCategory(@PathVariable String category) {
        return bookService.searchBooksByCategory(category);
    }
    
    @GetMapping("/status/{status}")
    public List<Books> getBooksByStatus(@PathVariable String status) {
        return bookService.getBooksByStatus(status);
    }
    
    @GetMapping("/available")
    public List<Books> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }
    
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Books> getBookByIsbn(@PathVariable String isbn) {
        Books book = bookService.getBookByIsbn(isbn);
        if (book != null) {
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/year/{year}")
    public List<Books> getBooksByYear(@PathVariable String year) {
        return bookService.getBooksByYear(year);
    }
    
    @GetMapping("/search")
    public List<Books> searchBooks(
            @RequestParam(required = false) String bookName,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category) {
        return bookService.searchBooks(bookName, author, category);
    }
    
    // Statistics endpoints
    @GetMapping("/count/total")
    public ResponseEntity<Long> getTotalBooks() {
        long count = bookService.getTotalBooks();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/available")
    public ResponseEntity<Long> getAvailableBooksCount() {
        long count = bookService.getAvailableBooksCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/borrowed")
    public ResponseEntity<Long> getBorrowedBooksCount() {
        long count = bookService.getBorrowedBooksCount();
        return ResponseEntity.ok(count);
    }
}
