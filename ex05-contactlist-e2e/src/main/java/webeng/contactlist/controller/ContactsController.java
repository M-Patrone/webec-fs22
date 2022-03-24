package webeng.contactlist.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import webeng.contactlist.service.ContactService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Size;

import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class ContactsController {

    private final ContactService service;

    @Value("${min.length.search}")
    private int minLength;

    private String searchText = null;

    public ContactsController(ContactService service) {
        this.service = service;
    }

    @GetMapping("/contacts")
    public String contacts( @RequestParam(required = false) String searchText,
                           Model model
     ) {
        if(searchText!=null && searchText.length()<minLength){
            throw new RuntimeException("searchtext has to be at minimum 5 char long");
        }
        this.searchText = searchText;
        model.addAttribute("contactList", service.searchContact(searchText));
        model.addAttribute("textToBeSearched", this.searchText);
        return "contacts";
    }

    @GetMapping("/contacts/{id}")
    public String showContact(@PathVariable int id, Model model) {
        var contact = service.findContact(id).orElseThrow(ContactNotFound::new);
        model.addAttribute("contactList", service.searchContact(this.searchText));
        model.addAttribute("textToBeSearched", this.searchText);
        model.addAttribute("contact", contact);
        return "contacts";
    }

    @GetMapping("/resetsearch")
    public String resetSearch() {
        this.searchText = null;
        return "redirect:contacts";
    }

    @ExceptionHandler(ContactNotFound.class)
    @ResponseStatus(NOT_FOUND)
    public String notFound(Model model) {
        model.addAttribute("contactList", service.getContactList());
        return "contacts";
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleResourceNotFoundException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage() + "\n");
        }
        return strBuilder.toString();
    }
    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleInValidSearch(RuntimeException e, Model model) {
        String violations = e.getMessage();
        model.addAttribute("contactList", service.searchContact(null));
        model.addAttribute("err", violations);
        return "contacts";
    }

    private static class ContactNotFound extends RuntimeException {
    }
}
