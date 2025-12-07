package com.spaza.content.api;

import com.spaza.content.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

public interface NewsletterApiCtrl {

    @PostMapping("/api/v1/newsletter")
    ResponseEntity<Void> subscribe(@Valid @RequestBody NewsletterSubscription subscription);

    @DeleteMapping("/api/v1/newsletter/{email}")
    ResponseEntity<Void> unsubscribe(@PathVariable String email);

    @PutMapping("/api/v1/newsletter/{email}")
    ResponseEntity<Void> update(@PathVariable String email, @Valid @RequestBody NewsletterSubscription subscription);

    @PostMapping("/api/v1/private/optin")
    ResponseEntity<Void> createOptin(@Valid @RequestBody PersistableOptin optin);
}
