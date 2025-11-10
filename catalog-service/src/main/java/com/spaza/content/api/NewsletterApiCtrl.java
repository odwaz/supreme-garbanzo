package com.spaza.content.api;

import com.spaza.content.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

public interface NewsletterApiCtrl {

    @PostMapping("/api/v1/newsletter")
    ResponseEntity<Void> subscribe(@Valid @RequestBody NewsletterSubscription subscription);

    @GetMapping("/api/v1/private/optin")
    ResponseEntity<ReadableOptin[]> listOptins();

    @DeleteMapping("/api/v1/private/optin/{email}")
    ResponseEntity<Void> deleteOptin(@PathVariable String email);
}
