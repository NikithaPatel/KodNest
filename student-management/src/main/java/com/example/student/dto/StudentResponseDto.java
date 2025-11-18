package com.example.student.dto;

import java.time.Instant;

public class StudentResponseDto {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String branch;
    private Integer yop;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean deleted;

    public StudentResponseDto() {
    }

    public StudentResponseDto(Long id, String fullName, String email, String phone, String branch,
                              Integer yop, boolean active, Instant createdAt, Instant updatedAt, boolean deleted) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.branch = branch;
        this.yop = yop;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
