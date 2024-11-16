package com.ajouchong.service;

import com.ajouchong.dto.request.AdminInfoRequestDto;
import com.ajouchong.entity.Admin;
import com.ajouchong.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public Admin saveAdminContactInfo(AdminInfoRequestDto contactInfo) {
        List<Admin> existingAdmins = adminRepository.findAll();

        if (!existingAdmins.isEmpty()) {
            adminRepository.deleteAll();
        }

        Admin admin = Admin.builder()
                .admin_name(contactInfo.getAdmin_name())
                .phone(contactInfo.getPhone())
                .address(contactInfo.getAddress())
                .time(contactInfo.getTime())
                .build();

        return adminRepository.save(admin);
    }

    public Optional<Admin> getAdminContactInfo() {
        List<Admin> admins = adminRepository.findAll();
        if (admins.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(admins.getFirst());
    }
}
