package com.spaza.content.delegate;

import com.spaza.content.model.*;
import org.springframework.http.ResponseEntity;

public interface NewsletterApiCtrlDelegate {
    ResponseEntity<Void> subscribe(NewsletterSubscription subscription);
    ResponseEntity<ReadableOptin[]> listOptins();
    ResponseEntity<Void> deleteOptin(String email);
}
