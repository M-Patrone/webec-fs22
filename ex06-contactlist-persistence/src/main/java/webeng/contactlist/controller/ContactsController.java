package webeng.contactlist.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import webeng.contactlist.model.Contact;
import webeng.contactlist.service.ContactService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@Validated
public class ContactsController {

    private final ContactService service;

    @Value("${contact-list.search.min-length}")
    private int minSearchLength;

    public ContactsController(ContactService service) {
        this.service = service;
    }

    @GetMapping("/contacts")
    public String contacts(String search, Model model) {
        checkSearch(search);
        model.addAttribute("contactList", service.getContactList(search));
        return "contacts";
    }

    @GetMapping("/contacts/{id}")
    public String showContact(@PathVariable int id, String search, Model model) {
        checkSearch(search);
        var contact = service.findContact(id).orElseThrow(ContactNotFound::new);
        model.addAttribute("contactList", service.getContactList(search));
        model.addAttribute("contact", contact);
        return "contacts";
    }

    @GetMapping("/contacts/add")
    public String addContact(Model model) {
        return "add-contact";
    }

    @PostMapping("/contacts/add")
    public String addContact(String firstName, String lastName, String jobTitle, String company,String newEmail) {
        ArrayList<String> allEmails = new ArrayList<>();
        if(newEmail!=null && !newEmail.isEmpty()){
            allEmails.add(newEmail);
        }

        service.add(firstName, lastName, jobTitle, company,allEmails);
        return "redirect:/contacts";
    }

    @GetMapping("/contacts/edit/{id}")
    public String editContact(@PathVariable int id, Model model) {
        Contact contactToEdit = service.findContact(id).orElseThrow(ContactNotFound::new);
        model.addAttribute("contactToEdit", contactToEdit);
        return "edit-contact";
    }

    @PostMapping("/contacts/edit/{id}")
    public String editContact(Contact contactToUpdate,String newEmail, HttpServletRequest request, Model model) {
        var listOfParams = request.getParameterMap();

        List<String> allEmails = new ArrayList<>();

        for (Map.Entry<String, String[]> param : listOfParams.entrySet()) {
            if(param.getKey().contains("emailMut") ){
                String email = param.getValue()[0];
                allEmails.add(email);
            }
        }
        for (Map.Entry<String, String[]> param : listOfParams.entrySet()) {
            if(param.getKey().contains("deleteEmail") ){
                boolean isOn = param.getValue()[0].equals("on")?true:false;
                if(isOn){
                    int indexToDelete = Integer.parseInt(param.getKey().replace("deleteEmail",""));
                    allEmails.remove(indexToDelete);
                }
            }
        }
        if(newEmail!=null && !newEmail.isEmpty()){
            allEmails.add(newEmail);
        }

        if(allEmails.size()!=0){
            contactToUpdate.setEmail(allEmails);
        }
        service.update(contactToUpdate);
        return "redirect:/contacts";
    }

    private void checkSearch(String search) {
        if (search != null && search.length() < minSearchLength) {
            throw new InvalidSearch();
        }
    }

    @ExceptionHandler(InvalidSearch.class)
    @ResponseStatus(BAD_REQUEST)
    public String invalidSearch(Model model) {
        model.addAttribute("contactList", service.getContactList(null));
        model.addAttribute("errorMessage",
            "Search text must have at least %s characters".formatted(minSearchLength));
        return "contacts";
    }

    @ExceptionHandler(ContactNotFound.class)
    @ResponseStatus(NOT_FOUND)
    public String notFound(Model model) {
        model.addAttribute("contactList", service.getContactList(null));
        model.addAttribute("errorMessage", "Contact not found");
        return "contacts";
    }

    private static class InvalidSearch extends RuntimeException {
    }

    private static class ContactNotFound extends RuntimeException {
    }
}
