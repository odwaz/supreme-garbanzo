package com.spaza.content.controller;

import com.spaza.content.api.NewsletterApiCtrl;
import com.spaza.content.delegate.NewsletterApiCtrlDelegate;
import com.spaza.content.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NewsletterApiCtrlController implements NewsletterApiCtrl {

    @Autowired
    private NewsletterApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<Void> subscribe(NewsletterSubscription subscription) {
        return delegate.subscribe(subscription);
    }

    @Override
    public ResponseEntity<Void> unsubscribe(String email) {
        return delegate.unsubscribe(email);
    }

    @Override
    public ResponseEntity<Void> update(String email, NewsletterSubscription subscription) {
        return delegate.update(email, subscription);
    }

    @Override
    public ResponseEntity<Void> createOptin(PersistableOptin optin) {
        return delegate.createOptin(optin);
    }
}
