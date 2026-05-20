package com.klef.Library.Management.Repository;

import com.klef.Library.Management.Modelclass.ContactModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactModel, String> {
}
