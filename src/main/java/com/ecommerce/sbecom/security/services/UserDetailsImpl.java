package com.ecommerce.sbecom.security.services;

import com.ecommerce.sbecom.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
// Custom UserDetails Class
public class UserDetailsImpl implements UserDetails {
    // This field acts as unique identifier for serializable class. It helps during deserialization to verify the sender
    // and receiver of the serialize object have loaded the same class version. It is to avoid invalid class exception
    // if the class structure changes overtime. And we are making it explicit rather than depending on java which creates
    // one automatically. Just like version control number.
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;

    // This ensures password is not serialized in JSON responses because password is sensitive info.
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, Collection<? extends GrantedAuthority> authorities, String username, String email,
                           String password) {
        this.id = id;
        this.authorities = authorities;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(
                role.getRoleName().name())).collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getUserId(),
                authorities,
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
