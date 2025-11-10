package com.spaza.content.model;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class NewsletterSubscription {
    @NotBlank
    @Email
    private String email;
    private String firstName;
    private String lastName;
}
