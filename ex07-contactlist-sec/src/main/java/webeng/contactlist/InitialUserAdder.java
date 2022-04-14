package webeng.contactlist;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;
import webeng.contactlist.model.Contact;
import webeng.contactlist.model.User;
import webeng.contactlist.model.UserRepository;
import webeng.contactlist.service.ContactService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;

@Component
public class InitialUserAdder {

    private static final Logger logger = LoggerFactory.getLogger(InitialUserAdder.class);
    private final UserRepository userRepository;

    public InitialUserAdder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws IOException {
        addInitialUser();
    }

    public void addInitialUser() throws IOException {
        if(userRepository.findAll().isEmpty()) {
            var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            var encodedPassword = encoder.encode("user");
            var user = new User("user", encodedPassword, emptySet());
            userRepository.save(user);
            logger.info("added sample user 'user'");
        }
    }
}
