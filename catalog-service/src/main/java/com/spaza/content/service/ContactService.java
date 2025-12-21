package com.spaza.content.service;

import com.spaza.content.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContactService {

    public void submitContact(ContactForm form) {
        log.info("Contact form submitted: name={}, email={}, subject={}", 
                 form.getName(), form.getEmail(), form.getSubject());
        // Email service integration would go here
    }
}
