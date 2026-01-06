package za.blkmarket.userauth.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    
    @Column(name = "admin_email", unique = true, nullable = false, length = 255)
    private String email;
    
    @Column(name = "admin_password", nullable = false, length = 60)
    private String password;
    
    @Column(name = "admin_first_name", length = 255)
    private String firstName;
    
    @Column(name = "admin_last_name", length = 255)
    private String lastName;
    
    @Column(name = "active")
    private boolean active = true;
    
    @Column(name = "date_created")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Column(name = "date_modified")
    private LocalDateTime updatedDate = LocalDateTime.now();
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_group",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> groups;
    
    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private MerchantStore merchantStore;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
    public List<Group> getGroups() { return groups; }
    public void setGroups(List<Group> groups) { this.groups = groups; }
    public MerchantStore getMerchantStore() { return merchantStore; }
    public void setMerchantStore(MerchantStore merchantStore) { this.merchantStore = merchantStore; }
    
    public boolean isSuperAdmin() {
        return groups != null && groups.stream().anyMatch(g -> "SUPERADMIN".equals(g.getName()));
    }
    
    public boolean isAdmin() {
        return groups != null && groups.stream().anyMatch(g -> "ADMIN".equals(g.getName()) || "ADMIN_STORE".equals(g.getName()) || "SUPERADMIN".equals(g.getName()));
    }
}