package com.klef.Library.Management.DTO;

public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String memberId;
    private String role;
    private Boolean isActive;

    public UserDTO(String id, String name, String email, String phone,
                   String memberId, String role, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.memberId = memberId;
        this.role = role;
        this.isActive = isActive;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getMemberId() { return memberId; }
    public String getRole() { return role; }
    public Boolean getIsActive() { return isActive; }
}
