package com.klef.Library.Management.Service;

import com.klef.Library.Management.Modelclass.BookModel;
import com.klef.Library.Management.Modelclass.IssueModel;
import com.klef.Library.Management.Modelclass.SignupModel;
import com.klef.Library.Management.Repository.IssueRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepo;
    private final Bookservice bookService;

    @Value("${app.reservation.expiry-days:3}")
    private int reservationExpiryDays;

    @Value("${app.max-borrow-days:14}")
    private int maxBorrowDays;

    public IssueService(IssueRepository issueRepo, Bookservice bookService) {
        this.issueRepo = issueRepo;
        this.bookService = bookService;
    }

    /** User submits a borrow request */
    @Transactional
    public IssueModel submitRequest(SignupModel user, String bookId, String userNote) {
        BookModel book = bookService.findById(bookId);

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("Book is currently out of stock.");
        }

        List<String> activeStatuses = List.of("pending", "approved", "collected");
        issueRepo.findByUserAndBookAndStatusIn(user, book, activeStatuses)
                 .ifPresent(e -> { throw new IllegalStateException(
                         "You already have an active request for this book."); });

        IssueModel req = new IssueModel();
        req.setUser(user);
        req.setBook(book);
        req.setUserNote(userNote);
        req.setStatus("pending");

        return issueRepo.save(req);
    }

    public List<IssueModel> getMyRequests(SignupModel user) {
        return issueRepo.findByUserOrderByCreatedAtDesc(user);
    }

    public List<IssueModel> getAllRequests(String status) {
        return (status != null && !status.isBlank())
            ? issueRepo.findByStatusOrderByCreatedAtDesc(status)
            : issueRepo.findAllByOrderByCreatedAtDesc();
    }

    /** Admin approves a pending request */
    @Transactional
    public IssueModel approve(String requestId, String adminNote) {
        IssueModel req = findById(requestId);
        if (!"pending".equals(req.getStatus())) {
            throw new IllegalStateException("Request is not in pending state.");
        }

        BookModel book = req.getBook();
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No copies available to approve.");
        }

        // Decrement available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookService.save(book);

        req.setStatus("approved");
        req.setAdminNote(adminNote);
        req.setRespondedAt(LocalDateTime.now());
        req.setReservedUntil(LocalDateTime.now().plusDays(reservationExpiryDays));

        return issueRepo.save(req);
    }

    /** Admin rejects a pending request */
    @Transactional
    public IssueModel reject(String requestId, String adminNote) {
        if (adminNote == null || adminNote.isBlank()) {
            throw new IllegalArgumentException("Admin note is required for rejection.");
        }

        IssueModel req = findById(requestId);
        if (!"pending".equals(req.getStatus())) {
            throw new IllegalStateException("Request is not in pending state.");
        }

        req.setStatus("rejected");
        req.setAdminNote(adminNote);
        req.setRespondedAt(LocalDateTime.now());

        return issueRepo.save(req);
    }

    /** Admin marks approved request as collected (physically handed over) */
    @Transactional
    public IssueModel markCollected(String requestId) {
        IssueModel req = findById(requestId);
        if (!"approved".equals(req.getStatus())) {
            throw new IllegalStateException("Request is not in approved state.");
        }

        req.setStatus("collected");
        req.setDueDate(LocalDateTime.now().plusDays(maxBorrowDays));

        return issueRepo.save(req);
    }

    /** Admin marks collected book as returned */
    @Transactional
    public IssueModel markReturned(String requestId) {
        IssueModel req = findById(requestId);
        if (!"collected".equals(req.getStatus())) {
            throw new IllegalStateException("Request is not in collected state.");
        }

        // Restore available copy count
        BookModel book = req.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookService.save(book);

        req.setStatus("returned");
        req.setReturnedAt(LocalDateTime.now());

        return issueRepo.save(req);
    }

    private IssueModel findById(String id) {
        return issueRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Borrow request not found."));
    }
}
