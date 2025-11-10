package com.spaza.content.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReadableOptin {
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime subscribedDate;
}
