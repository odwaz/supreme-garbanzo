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

    public ReadableOptin[] listOptins() {
        return repository.findAll().stream()
            .map(this::toReadable)
            .toArray(ReadableOptin[]::new);
    }

    public void deleteOptin(String email) {
        repository.findByEmail(email).ifPresent(repository::delete);
    }

    private ReadableOptin toReadable(Newsletter entity) {
        ReadableOptin readable = new ReadableOptin();
        readable.setEmail(entity.getEmail());
        readable.setFirstName(entity.getFirstName());
        readable.setLastName(entity.getLastName());
        readable.setSubscribedDate(entity.getSubscribedDate());
        return readable;
    }
}
