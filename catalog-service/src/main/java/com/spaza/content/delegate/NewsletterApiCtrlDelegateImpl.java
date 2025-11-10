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
    public ResponseEntity<ReadableOptin[]> listOptins() {
        return ResponseEntity.ok(newsletterService.listOptins());
    }

    @Override
    public ResponseEntity<Void> deleteOptin(String email) {
        newsletterService.deleteOptin(email);
        return ResponseEntity.ok().build();
    }
}
