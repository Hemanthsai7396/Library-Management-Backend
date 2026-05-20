package com.klef.Library.Management.DTO;

import com.klef.Library.Management.Modelclass.IssueModel;
import java.time.LocalDateTime;

public class BorrowRequestDTO {
    private String id;
    private String status;
    private String userNote;
    private String adminNote;
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
    private LocalDateTime reservedUntil;
    private LocalDateTime dueDate;
    private LocalDateTime returnedAt;
    private LocalDateTime createdAt;
    // Nested
    private BookSummary book;
    private UserSummary user;

    public static BorrowRequestDTO from(IssueModel m) {
        BorrowRequestDTO dto = new BorrowRequestDTO();
        dto.id = m.getId();
        dto.status = m.getStatus();
        dto.userNote = m.getUserNote();
        dto.adminNote = m.getAdminNote();
        dto.requestedAt = m.getRequestedAt();
        dto.respondedAt = m.getRespondedAt();
        dto.reservedUntil = m.getReservedUntil();
        dto.dueDate = m.getDueDate();
        dto.returnedAt = m.getReturnedAt();
        dto.createdAt = m.getCreatedAt();

        if (m.getBook() != null) {
            dto.book = new BookSummary(
                m.getBook().getId(), m.getBook().getTitle(),
                m.getBook().getAuthor(), m.getBook().getIsbn(),
                m.getBook().getCoverImage()
            );
        }
        if (m.getUser() != null) {
            dto.user = new UserSummary(
                m.getUser().getId(), m.getUser().getName(),
                m.getUser().getEmail(), m.getUser().getMemberId()
            );
        }
        return dto;
    }

    // ─── Nested DTOs ────────────────────────────────────
    public static class BookSummary {
        public String id, title, author, isbn, coverImage;
        public BookSummary(String id, String title, String author,
                           String isbn, String coverImage) {
            this.id = id; this.title = title; this.author = author;
            this.isbn = isbn; this.coverImage = coverImage;
        }
    }

    public static class UserSummary {
        public String id, name, email, memberId;
        public UserSummary(String id, String name, String email, String memberId) {
            this.id = id; this.name = name;
            this.email = email; this.memberId = memberId;
        }
    }

    // ─── Getters ────────────────────────────────────────
    public String getId() { return id; }
    public String getStatus() { return status; }
    public String getUserNote() { return userNote; }
    public String getAdminNote() { return adminNote; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public LocalDateTime getRespondedAt() { return respondedAt; }
    public LocalDateTime getReservedUntil() { return reservedUntil; }
    public LocalDateTime getDueDate() { return dueDate; }
    public LocalDateTime getReturnedAt() { return returnedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public BookSummary getBook() { return book; }
    public UserSummary getUser() { return user; }
}
