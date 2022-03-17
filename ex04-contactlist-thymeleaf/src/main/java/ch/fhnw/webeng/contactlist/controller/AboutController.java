package ch.fhnw.webeng.contactlist.controller;

import ch.fhnw.webeng.contactlist.service.ContactService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    private ContactService service;
    public AboutController(ContactService service) {
        this.service = service;
    }

    @GetMapping("/about")
    public String getAboutPage(Model m){
        m.addAttribute("CountOfContact", service.getContactList().size());
        m.addAttribute("CountOfPhone", service.getNumberOfPhoneNumbers());
        m.addAttribute("CountOfEmail", service.getNumberOfEmails());
        return "about";
    }
}
