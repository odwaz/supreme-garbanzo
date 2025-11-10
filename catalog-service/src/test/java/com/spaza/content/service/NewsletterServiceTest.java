package com.spaza.content.service;

import com.spaza.content.model.*;
import com.spaza.content.repository.NewsletterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsletterServiceTest {

    @Mock
    private NewsletterRepository repository;

    @InjectMocks
    private NewsletterService service;

    private Newsletter newsletter;
    private NewsletterSubscription subscription;

    @BeforeEach
    void setUp() {
        newsletter = new Newsletter();
        newsletter.setId(1L);
        newsletter.setEmail("test@example.com");
        newsletter.setFirstName("John");
        newsletter.setLastName("Doe");
        newsletter.setSubscribedDate(LocalDateTime.now());

        subscription = new NewsletterSubscription();
        subscription.setEmail("test@example.com");
        subscription.setFirstName("John");
        subscription.setLastName("Doe");
    }

    @Test
    void subscribe_ShouldSaveSubscription() {
        when(repository.save(any(Newsletter.class))).thenReturn(newsletter);

        service.subscribe(subscription);

        verify(repository).save(any(Newsletter.class));
    }

    @Test
    void listOptins_ShouldReturnAllOptins() {
        when(repository.findAll()).thenReturn(Arrays.asList(newsletter));

        ReadableOptin[] result = service.listOptins();

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("test@example.com", result[0].getEmail());
        assertEquals("John", result[0].getFirstName());
        verify(repository).findAll();
    }

    @Test
    void deleteOptin_ShouldDeleteOptin_WhenExists() {
        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(newsletter));

        service.deleteOptin("test@example.com");

        verify(repository).findByEmail("test@example.com");
        verify(repository).delete(newsletter);
    }

    @Test
    void deleteOptin_ShouldDoNothing_WhenNotExists() {
        when(repository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        service.deleteOptin("test@example.com");

        verify(repository).findByEmail("test@example.com");
        verify(repository, never()).delete(any());
    }
}
