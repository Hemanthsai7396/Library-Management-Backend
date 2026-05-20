package com.klef.Library.Management.Modelclass;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a borrow request (pre-booking) placed by a user.
 * Status flow: pending -> approved -> collected -> returned
 *              pending -> rejected
 */
@Entity
@Table(name = "borrow_requests")
public class IssueModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private SignupModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookModel book;

    /** pending | approved | rejected | collected | returned | expired */
    @Column(nullable = false)
    private String status = "pending";

    @Column(name = "user_note")
    private String userNote;

    @Column(name = "admin_note")
    private String adminNote;

    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt = LocalDateTime.now();

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @Column(name = "reserved_until")
    private LocalDateTime reservedUntil;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() { this.updatedAt = LocalDateTime.now(); }

    // ─── Getters & Setters ────────────────────────────────────
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public SignupModel getUser() { return user; }
    public void setUser(SignupModel user) { this.user = user; }

    public BookModel getBook() { return book; }
    public void setBook(BookModel book) { this.book = book; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserNote() { return userNote; }
    public void setUserNote(String userNote) { this.userNote = userNote; }

    public String getAdminNote() { return adminNote; }
    public void setAdminNote(String adminNote) { this.adminNote = adminNote; }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }

    public LocalDateTime getReservedUntil() { return reservedUntil; }
    public void setReservedUntil(LocalDateTime reservedUntil) { this.reservedUntil = reservedUntil; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public LocalDateTime getReturnedAt() { return returnedAt; }
    public void setReturnedAt(LocalDateTime returnedAt) { this.returnedAt = returnedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
