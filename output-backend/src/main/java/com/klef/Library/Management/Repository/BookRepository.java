package com.klef.Library.Management.Repository;

import com.klef.Library.Management.Modelclass.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookModel, String> {
    List<BookModel> findByTitleContainingIgnoreCase(String title);
    List<BookModel> findByAuthorContainingIgnoreCase(String author);
    List<BookModel> findByGenreContainingIgnoreCase(String genre);
    List<BookModel> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenreContainingIgnoreCase(
        String title, String author, String genre);
    boolean existsByIsbn(String isbn);
}
