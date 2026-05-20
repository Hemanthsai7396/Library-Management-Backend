package com.klef.Library.Management.Repository;

import com.klef.Library.Management.Modelclass.SignupModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface signupRepository extends JpaRepository<SignupModel, String> {
    Optional<SignupModel> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByMemberId(String memberId);
}
