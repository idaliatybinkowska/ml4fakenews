package com.ml4fakenews.accounts.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
public class User implements UserDetails {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String username;
        private String password;
        private String email;
        private boolean isVerified;
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
        private Set<Role> roles = new HashSet<>();

    public User(final int id, final String username, final String email, final boolean isVerified){
        this.id = id;
        this.username = username;
        this.email = email;
        this.isVerified = isVerified;
    }

    public User(final String username, final String password, final String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.isVerified = false;
    }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities () {
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRolename())));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void verify () {
        this.isVerified = true;
    }
    }
