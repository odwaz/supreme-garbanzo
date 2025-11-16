package za.blkmarket.userauth.dto;

public class PersistableMerchantStore {
    private String code;
    private String name;
    private String email;
    private String phone;
    private PersistableAddress address;
    private Long parentId;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public PersistableAddress getAddress() { return address; }
    public void setAddress(PersistableAddress address) { this.address = address; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
}
