package webeng.contactlist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webeng.contactlist.model.Contact;
import webeng.contactlist.service.ContactService;

import java.net.URI;
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

    @PutMapping("{id}")
    public ResponseEntity<Void> put(@PathVariable int id,@RequestBody Contact newContact){
        Optional<Contact> contact = contactService.findContact(id);
        if(contact.isEmpty()){
            contactService.add(newContact);
        }else{
            contactService.update(newContact);
        }
        ResponseEntity.BodyBuilder body = ResponseEntity.ok();
        return body.build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        Optional<Contact> contact = contactService.findContact(id);
        if(contact.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            contactService.delete(contact.get());
            ResponseEntity.BodyBuilder body = ResponseEntity.ok();
            return body.build();
        }
    }

    @PostMapping()
    public ResponseEntity<String> post(@RequestBody Contact newContact){
        Contact c = contactService.add(newContact);
        return ResponseEntity.created(URI.create("/api/contacts/"+c.getId())).build();
    }

}
