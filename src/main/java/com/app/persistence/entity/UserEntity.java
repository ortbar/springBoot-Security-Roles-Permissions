package com.app.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(name = "is_Enabled")
    private boolean isEnabled;

    @Column(name = "account_NO_Expired")
    private boolean accountNotExpired;

    @Column(name = "account_NO_Locked")
    private boolean accountNotLocked;

    @Column(name = "credential_NO_Expired")
    private boolean credentialNotExpired;

    // relacion con los roles, muchos a muchos
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name= "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();



}
