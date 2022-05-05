package webeng.contactlist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import webeng.contactlist.model.Contact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class ContactsRestControllerIT {

    @LocalServerPort
    int port;
    
    @Autowired
    TestRestTemplate rest;

    @Test
    public void testGetAll(){
        ResponseEntity<String> response = rest.getForEntity("/api/contacts", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Kennifick"));
    }
    @Test
    public void testGet(){
        Contact c = rest.getForObject("/api/contacts/3", Contact.class);

        assertEquals("Mabel",c.getFirstName());
        assertEquals("Guppy",c.getLastName());
    }


}
