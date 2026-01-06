package com.spaza.content.delegate;

import com.spaza.content.model.*;
import com.spaza.content.service.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsletterApiCtrlDelegateImpl implements NewsletterApiCtrlDelegate {

    private final NewsletterService newsletterService;

    @Override
    public ResponseEntity<Void> subscribe(NewsletterSubscription subscription) {
        newsletterService.subscribe(subscription);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> unsubscribe(String email) {
        newsletterService.unsubscribe(email);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> update(String email, NewsletterSubscription subscription) {
        newsletterService.update(email, subscription);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> createOptin(PersistableOptin optin) {
        newsletterService.createOptin(optin);
        return ResponseEntity.ok().build();
    }
}
