package com.example.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class StudentRequestDto {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email address")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone must contain 7-15 digits and optional leading +")
    private String phone;

    @NotBlank(message = "Branch is required")
    private String branch;

    @Min(value = 1900, message = "Year of passing must be after 1900")
    @Max(value = 2100, message = "Year of passing must be before 2100")
    private Integer yop;

    private boolean active;

    private boolean deleted;

    public StudentRequestDto() {
    }

    public StudentRequestDto(String fullName, String email, String phone, String branch,
                             Integer yop, boolean active, boolean deleted) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.branch = branch;
        this.yop = yop;
        this.active = active;
        this.deleted = deleted;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getYop() {
        return yop;
    }

    public void setYop(Integer yop) {
        this.yop = yop;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
