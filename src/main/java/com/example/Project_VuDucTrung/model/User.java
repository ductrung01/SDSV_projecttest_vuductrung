package com.example.Project_VuDucTrung.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long userid;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String address;
    private String phone;
    private String role;
}