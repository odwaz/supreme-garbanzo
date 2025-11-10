package com.spaza.content.controller;

import com.spaza.content.api.NewsletterApiCtrl;
import com.spaza.content.delegate.NewsletterApiCtrlDelegate;
import com.spaza.content.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class NewsletterApiCtrlController implements NewsletterApiCtrl {

    @Autowired
    private NewsletterApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<Void> subscribe(NewsletterSubscription subscription) {
        return delegate.subscribe(subscription);
    }

    @Override
    public ResponseEntity<ReadableOptin[]> listOptins() {
        return delegate.listOptins();
    }

    @Override
    public ResponseEntity<Void> deleteOptin(String email) {
        return delegate.deleteOptin(email);
    }
}
