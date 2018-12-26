package com.darrinhowell.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String getProfiles(@PathVariable long profileId, Model m, Principal p){

        ApplicationUser currentUser = appUserRepo.findByUsername(p.getName());

        System.out.println("This is the profileID" + profileId);
        System.out.println("This is the principleID" + currentUser.id);

        m.addAttribute("profile", appUserRepo.findById(profileId).get());
        m.addAttribute("principleID", currentUser.id);
        m.addAttribute("posts", appUserRepo.findById(profileId).get().postSet);

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

        ApplicationUser currentUser = appUserRepo.findByUsername(p.getName());
        m.addAttribute("profile", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
        m.addAttribute("posts", currentUser.postSet);
        m.addAttribute("principleID", currentUser.id);

        return "individualProfile";
    }

    @RequestMapping(value = "/blogPost/{userId}", method = RequestMethod.POST)
    public RedirectView createPost(@PathVariable long userId, @RequestParam String blogPostBody) {
        Post newPost = new Post(blogPostBody, new Date());
        newPost.user = appUserRepo.findById(userId).get();
        userPostRepo.save(newPost);
        return new RedirectView("/users/" + userId + "/show");
    }
}
