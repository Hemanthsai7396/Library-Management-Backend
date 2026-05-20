package com.klef.Library.Management.Repository;

import com.klef.Library.Management.Modelclass.IssueModel;
import com.klef.Library.Management.Modelclass.SignupModel;
import com.klef.Library.Management.Modelclass.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<IssueModel, String> {
    List<IssueModel> findByUserOrderByCreatedAtDesc(SignupModel user);
    List<IssueModel> findAllByOrderByCreatedAtDesc();
    List<IssueModel> findByStatusOrderByCreatedAtDesc(String status);
    Optional<IssueModel> findByUserAndBookAndStatusIn(SignupModel user, BookModel book, List<String> statuses);
}
