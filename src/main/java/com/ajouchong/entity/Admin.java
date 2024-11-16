package com.ajouchong.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long admin_id;

    private String admin_name;
    private String phone;
    private String address;
    private String time;

    @Builder
    public Admin(String admin_name, String phone, String address, String time) {
        this.admin_name = admin_name;
        this.phone = phone;
        this.address = address;
        this.time = time;
    }
}
