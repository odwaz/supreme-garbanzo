package com.spaza.content.service;

import com.spaza.content.model.*;
import com.spaza.content.repository.NewsletterRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class NewsletterService {

    private final NewsletterRepository repository;

    public NewsletterService(NewsletterRepository repository) {
        this.repository = repository;
    }

    public void subscribe(NewsletterSubscription subscription) {
        Newsletter entity = new Newsletter();
        entity.setEmail(subscription.getEmail());
        entity.setFirstName(subscription.getFirstName());
        entity.setLastName(subscription.getLastName());
        entity.setSubscribedDate(LocalDateTime.now());
        repository.save(entity);
    }

    public void unsubscribe(String email) {
        repository.findByEmail(email).ifPresent(repository::delete);
    }

    public void update(String email, NewsletterSubscription subscription) {
        repository.findByEmail(email).ifPresent(entity -> {
            entity.setFirstName(subscription.getFirstName());
            entity.setLastName(subscription.getLastName());
            repository.save(entity);
        });
    }

    public void createOptin(PersistableOptin optin) {
        Newsletter entity = new Newsletter();
        entity.setEmail(optin.getEmail());
        entity.setFirstName(optin.getFirstName());
        entity.setLastName(optin.getLastName());
        entity.setSubscribedDate(LocalDateTime.now());
        repository.save(entity);
    }
}
