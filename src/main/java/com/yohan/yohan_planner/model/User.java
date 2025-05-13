package com.yohan.yohan_planner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username can not be blank")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
    @Column(nullable = false)
    private String passwordHash;

    @NotBlank(message = "First name can not be blank")
    @Column(nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name can not be blank")
    @Column(nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "Email can not be blank")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Timezone is required")
    @Column(nullable = false)
    private String timezone;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdDate;

    @Column(nullable = false)
    private ZonedDateTime updatedDate;

    public User() {

    }

    public User(String username, String passwordHash, String firstName, String lastName, String email, String timezone) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.timezone = timezone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getTimezone() { return timezone;}

    public Set<Role> getRoles() {
        return roles;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }


    @PrePersist
    public void prePersist() {
        ZonedDateTime currentTimeInUserZone = getCurrentTimeInUserZone();
        createdDate = currentTimeInUserZone;
        updatedDate = currentTimeInUserZone;
        enabled = true;
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = getCurrentTimeInUserZone();
    }

    public ZonedDateTime getCurrentTimeInUserZone() {
        return ZonedDateTime.now(getZoneId());
    }

    public ZoneId getZoneId() {
        return ZoneId.of(timezone);
    }
}
