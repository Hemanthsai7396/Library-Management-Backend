package com.klef.Library.Management.Service;

import com.klef.Library.Management.Modelclass.ContactModel;
import com.klef.Library.Management.Repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepo;

    public ContactService(ContactRepository contactRepo) {
        this.contactRepo = contactRepo;
    }

    public ContactModel submit(ContactModel ticket) {
        return contactRepo.save(ticket);
    }

    public List<ContactModel> findAll() {
        return contactRepo.findAll();
    }
}
