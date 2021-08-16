package rw.xyz.notifyapp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {
    /**
     *
     */
    private static final long serialVersionUID = -2051711900606460528L;
    
    private String email;
    private String password;
    private Boolean active;
    private User user;
    
    public MyUserDetails(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.active = user.getActive();
        this.user = user;
    }

    public MyUserDetails() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
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

    @Override
    public boolean isAccountNonExpired() {

        return active;
    }

    @Override
    public boolean isAccountNonLocked() {

        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return active;
    }

    @Override
    public boolean isEnabled() {

        return active;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}