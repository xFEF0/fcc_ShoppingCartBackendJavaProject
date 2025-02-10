package com.xfef0.fccshops.security.user;

import com.xfef0.fccshops.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class ShopUserDetails implements UserDetails {

    public static final String ROLE_ = "ROLE_";
    private Long id;
    private String email;
    private String password;
    private Collection<GrantedAuthority> authorities;

    public static ShopUserDetails buildUserDetails(User user) {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_ + role.getName()))
                .collect(Collectors.toSet());

        return new ShopUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
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
        return email;
    }

}
