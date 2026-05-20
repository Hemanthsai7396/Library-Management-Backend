package com.klef.Library.Management.Controller;

import com.klef.Library.Management.DTO.ApiResponse;
import com.klef.Library.Management.DTO.BookRequest;
import com.klef.Library.Management.Modelclass.BookModel;
import com.klef.Library.Management.Service.Bookservice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Handles: GET    /api/books
 *          GET    /api/books/{id}
 *          POST   /api/books          (admin)
 *          PUT    /api/books/{id}     (admin)
 *          DELETE /api/books/{id}     (admin)
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final Bookservice bookService;

    public BookController(Bookservice bookService) {
        this.bookService = bookService;
    }

    /** GET /api/books?title=&author=&genre= */
    @GetMapping
    public ResponseEntity<?> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre) {
        try {
            List<BookModel> books = bookService.findAll(title, author, genre);
            return ResponseEntity.ok(ApiResponse.ok(books));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error fetching books."));
        }
    }

    /** GET /api/books/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable String id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(bookService.findById(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error fetching book."));
        }
    }

    /** POST /api/books (admin only) */
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookRequest req) {
        try {
            BookModel book = bookService.create(req);
            return ResponseEntity.status(201).body(ApiResponse.ok(book));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error creating book."));
        }
    }

    /** PUT /api/books/{id} (admin only) */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable String id,
                                        @RequestBody BookRequest req) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(bookService.update(id, req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error updating book."));
        }
    }

    /** DELETE /api/books/{id} (admin only) */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id) {
        try {
            bookService.delete(id);
            return ResponseEntity.ok(ApiResponse.ok(Map.of("message", "Book deleted successfully")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Server error deleting book."));
        }
    }
}
