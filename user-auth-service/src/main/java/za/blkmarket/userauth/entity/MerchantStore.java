package za.blkmarket.userauth.entity;

import javax.persistence.*;

@Entity
@Table(name = "MERCHANT_STORE")
public class MerchantStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MERCHANT_ID")
    private Long id;
    
    @Column(name = "STORE_CODE", unique = true, nullable = false, length = 100)
    private String code;
    
    @Column(name = "STORE_NAME", length = 100)
    private String name;
    
    @Column(name = "STORE_EMAIL", length = 60)
    private String email;
    
    @Column(name = "STORE_PHONE", length = 50)
    private String phone;
    
    @Column(name = "STORE_ADDRESS", length = 255)
    private String address;
    
    @Column(name = "STORE_CITY", length = 100)
    private String city;
    
    @Column(name = "STORE_STATE_PROV", length = 100)
    private String stateProvince;
    
    @Column(name = "STORE_POSTAL_CODE", length = 15)
    private String postalCode;
    
    @Column(name = "STORE_COUNTRY", length = 2)
    private String country;
    
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private MerchantStore parent;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getStateProvince() { return stateProvince; }
    public void setStateProvince(String stateProvince) { this.stateProvince = stateProvince; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public MerchantStore getParent() { return parent; }
    public void setParent(MerchantStore parent) { this.parent = parent; }
}
