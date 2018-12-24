package com.darrinhowell.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class ApplicationUserController {

    ///////////////////////////////// -- Controller instance variables

    @Autowired
    public ApplicationUserRepository appUserRepo;

    @Autowired
    public PostRepository userPostRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    ///////////////////////////////// -- Routes


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showSplash(){
        return "index";
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.GET)
    public String showSignUp() { return "sign-up"; }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin() { return "login"; }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getProfiles(Model m){
        m.addAttribute("profiles", appUserRepo.findAll());
        return "users";
    }

    @RequestMapping(value = "/users/{profileId}/show", method = RequestMethod.GET)
    public String getProfiles(@PathVariable long profileId, Model m){
        m.addAttribute("profile", appUserRepo.findById(profileId).get());
        return "individualProfile";
    }


    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public RedirectView addUsers(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String dateOfBirth,
                                 @RequestParam String bio) {

        String hashedPassword = bCryptPasswordEncoder.encode(password);
        ApplicationUser newUser = new ApplicationUser(username, hashedPassword, firstName, lastName, dateOfBirth, bio);

        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null,
                new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        appUserRepo.save(newUser);
        return new RedirectView("/");
    }

    @GetMapping("/myProfile")
    public String loadUserProfile(Principal p, Model m){
        m.addAttribute("profile", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
        return "individualProfile";
    }

    @RequestMapping(value = "/blogPost/{userId}", method = RequestMethod.POST)
    public RedirectView createPost(@PathVariable long userId, @RequestParam String blogPostBody) {
        Post newPost = new Post(blogPostBody, new Date());
        newPost.user = appUserRepo.findById(userId).get();
        userPostRepo.save(newPost);

        ApplicationUser currentUser = appUserRepo.findById(userId).get();
        for(Post post : currentUser.postSet)
        System.out.println(post);

        return new RedirectView("/");
    }
}
