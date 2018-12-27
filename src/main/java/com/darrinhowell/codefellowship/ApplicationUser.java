package com.darrinhowell.codefellowship;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String dateOfBirth;
    public String bio;

    @OneToMany(mappedBy = "user")
    public List<Post> postSet = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "FollowUserTable",
            joinColumns = {@JoinColumn(name = "follower")},
            inverseJoinColumns = {@JoinColumn(name = "userBeingFollowed")}
    )
    Set<ApplicationUser> followerSet;

    @ManyToMany(mappedBy = "followerSet")
    Set<ApplicationUser> userBeingFollowed;


    public ApplicationUser (String username, String password, String firstName, String lastName,
                           String dateOfBirth, String bio) {

        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;

    }

    public ApplicationUser () {}

    public String toString(){
        return "Username is: " + this.username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
}
