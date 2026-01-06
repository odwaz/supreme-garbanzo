package za.blkmarket.userauth.dto;

public class ReadableMerchantStore {
    private Long id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private ReadableAddress address;
    private Long parentId;
    private String parentCode;

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
    public ReadableAddress getAddress() { return address; }
    public void setAddress(ReadableAddress address) { this.address = address; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getParentCode() { return parentCode; }
    public void setParentCode(String parentCode) { this.parentCode = parentCode; }
}
