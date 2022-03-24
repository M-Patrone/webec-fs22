package webeng.contactlist.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import webeng.contactlist.model.Contact;
import webeng.contactlist.model.ContactListEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class ContactService {

    private static final String JSON_FILE = "contacts.json";

    private final Map<Integer, Contact> contacts;

    public ContactService(ObjectMapper mapper) throws IOException {
        var contactsList = mapper.readValue(ContactService.class.getResource(JSON_FILE),
                new TypeReference<List<Contact>>() {});
        contacts = contactsList.stream()
                .collect(toMap(Contact::getId, identity()));
    }

    public List<ContactListEntry> getContactList() {
        return contacts.values().stream()
                .sorted(comparing(Contact::getId))
                .map(c -> new ContactListEntry(c.getId(), c.getFirstName() + " " + c.getLastName()))
                .collect(toList());
    }

    public Optional<Contact> findContact(int id) {
        return Optional.ofNullable(contacts.get(id));
    }

    public int phoneNumberCount() {
        return contacts.values().stream()
                .mapToInt(c -> c.getPhone().size())
                .sum();
    }

    public int emailCount() {
        return contacts.values().stream()
                .mapToInt(c -> c.getEmail().size())
                .sum();
    }

    public List<ContactListEntry> searchContact(String searchText){
        if(searchText==null || searchText.isEmpty()){
            return this.getContactList();
        }
        searchText = searchText.toLowerCase();
        var resutl = new ArrayList<ContactListEntry>();
        for(var c: contacts.values()){
            if(matches(searchText, c)
            ){
                resutl.add(new ContactListEntry(c.getId(), c.getFirstName() + " " + c.getLastName()));
            }
        }
        return resutl;
    }

    private boolean matches(String searchText, Contact contact) {
        boolean bCompany = contact.getCompany() != null && contact.getCompany().toLowerCase().contains(searchText);
        boolean bJobTitle = contact.getJobTitle() != null && contact.getJobTitle().toLowerCase().contains(searchText);

        return contact.getFirstName().toLowerCase().contains(searchText) ||
            contact.getLastName().toLowerCase().contains(searchText) ||
            bCompany ||
            bJobTitle ||
            listContains(contact.getEmail(), searchText) ||
            listContains(contact.getPhone(), searchText);
    }

    private boolean listContains(List<String> email, String searchText) {
        for(var element: email){
            if(element.toLowerCase().contains(searchText)){
                return true;
            }
        }
        return false;
    }
}
