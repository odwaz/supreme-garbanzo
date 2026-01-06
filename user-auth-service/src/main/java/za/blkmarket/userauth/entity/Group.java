package za.blkmarket.userauth.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_role")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    
    @Column(name = "role_name", unique = true, nullable = false, length = 255)
    private String name;
    
    @Column(name = "group_type", length = 255)
    private String type;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "permission_group",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public List<Permission> getPermissions() { return permissions; }
    public void setPermissions(List<Permission> permissions) { this.permissions = permissions; }
}