package com.darrinhowell.codefellowship;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    ApplicationUserRepository appRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = this.appRepo.findByUsername(username);

        if(user == null){
            System.out.println("User: " + username + " not found.");
            throw new UsernameNotFoundException("User (" + username + ") was not found in database.");
        }

        System.out.println("Found User: " + user);

        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), new ArrayList<>());

        return userDetails;
    }

}
