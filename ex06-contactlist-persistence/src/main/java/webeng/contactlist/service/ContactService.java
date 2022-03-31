package webeng.contactlist.service;

import org.springframework.stereotype.Service;
import webeng.contactlist.model.Contact;
import webeng.contactlist.model.ContactListEntry;
import webeng.contactlist.model.ContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.Locale.ROOT;
import static java.util.stream.Collectors.toList;

@Service
public class ContactService {


    private final ContactRepository repo;

    public ContactService(ContactRepository repo) {
        this.repo = repo;
    }

    public List<ContactListEntry> getContactList(String search) {
        return this.repo.findAll().stream()
                .filter(c -> matches(c, search))
                .sorted(comparing(Contact::getId))
                .map(c -> new ContactListEntry(c.getId(), c.getFirstName() + " " + c.getLastName()))
                .collect(toList());
    }

    private boolean matches(Contact contact, String search) {
        return search == null ||
                matches(contact.getFirstName(), search) ||
                matches(contact.getLastName(), search) ||
                contact.getEmail().stream().anyMatch(e -> matches(e, search)) ||
                contact.getPhone().stream().anyMatch(p -> matches(p, search)) ||
                matches(contact.getJobTitle(), search) ||
                matches(contact.getCompany(), search);
    }

    private boolean matches(String text, String search) {
        return text != null && text.toLowerCase(ROOT).contains(search.toLowerCase(ROOT));
    }

    public Optional<Contact> findContact(int id) {
        return this.repo.findById(id);
    }

    public int phoneNumberCount() {
        return this.repo.findAll().stream()
                .mapToInt(c -> c.getPhone().size())
                .sum();
    }

    public int emailCount() {
        return this.repo.findAll().stream()
                .mapToInt(c -> c.getEmail().size())
                .sum();
    }

    public Contact add(String firstName, String lastName,
                       String jobTitle, String company, ArrayList<String> email) {
        var contact = new Contact();
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setJobTitle(jobTitle);
        contact.setCompany(company);
        contact.setEmail(email);
        return add(contact);
    }

    public Contact add(Contact contact) {
        this.repo.save(contact);
        return contact; // important for later, when using Repository
    }
    public Contact update(Contact contactToUpdate){
        this.repo.save(contactToUpdate);
        return contactToUpdate;
    }
}
