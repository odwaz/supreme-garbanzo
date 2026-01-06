package com.spaza.content.model;

import lombok.Data;

@Data
public class ContactForm {
    private String name;
    private String email;
    private String subject;
    private String message;
}