package com.spaza.content.delegate;

import com.spaza.content.model.*;
import org.springframework.http.ResponseEntity;

public interface NewsletterApiCtrlDelegate {
    ResponseEntity<Void> subscribe(NewsletterSubscription subscription);
    ResponseEntity<Void> unsubscribe(String email);
    ResponseEntity<Void> update(String email, NewsletterSubscription subscription);
    ResponseEntity<Void> createOptin(PersistableOptin optin);
}
