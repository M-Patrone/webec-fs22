package webeng.contactlist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webeng.contactlist.model.Contact;
import webeng.contactlist.service.ContactService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
public class ContactsRestController {
    private ContactService contactService;

    public ContactsRestController(ContactService service){
        this.contactService = service;
    }

    @GetMapping()
    public List<Contact> getAll(){
        return contactService.getAllContacts();
    }

    @GetMapping("{id}") // beduetete variabel
    public ResponseEntity<Contact> get(@PathVariable int id){
        return ResponseEntity.of(contactService.findContact(id));
    }

}
