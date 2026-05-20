package com.klef.Library.Management.Service;

import com.klef.Library.Management.DTO.BookRequest;
import com.klef.Library.Management.Modelclass.BookModel;
import com.klef.Library.Management.Repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Bookservice {

    private final BookRepository bookRepo;

    public Bookservice(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<BookModel> findAll(String title, String author, String genre) {
        boolean hasTitle  = title  != null && !title.isBlank();
        boolean hasAuthor = author != null && !author.isBlank();
        boolean hasGenre  = genre  != null && !genre.isBlank();

        if (!hasTitle && !hasAuthor && !hasGenre) {
            return bookRepo.findAll();
        }
        return bookRepo.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenreContainingIgnoreCase(
            hasTitle  ? title  : "",
            hasAuthor ? author : "",
            hasGenre  ? genre  : ""
        );
    }

    public BookModel findById(String id) {
        return bookRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));
    }

    public BookModel create(BookRequest req) {
        int copies = (req.getTotalCopies() != null && req.getTotalCopies() > 0)
                ? req.getTotalCopies() : 1;

        BookModel book = new BookModel();
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setIsbn(req.getIsbn());
        book.setGenre(req.getGenre());
        book.setDescription(req.getDescription());
        book.setCoverImage(req.getCoverImage());
        book.setShelfLocation(req.getShelfLocation());
        book.setTotalCopies(copies);
        book.setAvailableCopies(copies);

        return bookRepo.save(book);
    }

    public BookModel update(String id, BookRequest req) {
        BookModel book = findById(id);
        if (req.getTitle()         != null) book.setTitle(req.getTitle());
        if (req.getAuthor()        != null) book.setAuthor(req.getAuthor());
        if (req.getIsbn()          != null) book.setIsbn(req.getIsbn());
        if (req.getGenre()         != null) book.setGenre(req.getGenre());
        if (req.getDescription()   != null) book.setDescription(req.getDescription());
        if (req.getCoverImage()    != null) book.setCoverImage(req.getCoverImage());
        if (req.getShelfLocation() != null) book.setShelfLocation(req.getShelfLocation());
        if (req.getTotalCopies()   != null) book.setTotalCopies(req.getTotalCopies());
        return bookRepo.save(book);
    }

    public void delete(String id) {
        bookRepo.delete(findById(id));
    }

    public BookModel save(BookModel book) {
        return bookRepo.save(book);
    }
}
