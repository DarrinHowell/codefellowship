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

import javax.websocket.server.PathParam;
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
        ApplicationUser profileUser = appUserRepo.findById(profileId).get();

        m.addAttribute("profile", profileUser);

        if(profileUser.followerSet == null) {
            m.addAttribute("numProfileFollowers", 0);
        } else {
            m.addAttribute("numProfileFollowers", profileUser.followerSet.size());
        }

        m.addAttribute("principleID", currentUser.id);
        m.addAttribute("posts", appUserRepo.findById(profileId).get().postSet);


        return "individualProfile";
    }


    @RequestMapping(value = "/newUsers", method = RequestMethod.POST)
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

        if(currentUser.followerSet == null) {
            m.addAttribute("numProfileFollowers", 0);
        } else {
            m.addAttribute("numProfileFollowers", currentUser.followerSet.size());
        }

        m.addAttribute("posts", currentUser.postSet);
        m.addAttribute("principleID", currentUser.id);

        return "individualProfile";
    }

    @RequestMapping(value = "/blogPost/{userId}", method = RequestMethod.POST)
    public RedirectView createPost(@PathVariable long userId, @RequestParam String blogPostBody) {

        if(blogPostBody.toUpperCase().contains("DROP TABLE") ||
                blogPostBody.toUpperCase().contains("UNION SELECT USERNAME, PASSWORD") ||
                blogPostBody.toUpperCase().contains("<SCRIPT>") ||
                blogPostBody.toUpperCase().contains("</SCRIPT>")){
            return new RedirectView("/securityCheck");

        } else {
            Post newPost = new Post(blogPostBody, new Date());
            newPost.user = appUserRepo.findById(userId).get();
            userPostRepo.save(newPost);
            return new RedirectView("/users/" + userId + "/show");
        }
    }

    @GetMapping("/securityCheck")
    public String securityWarning(){
        return "securityWarning";
    }

    @RequestMapping(value = "/followManager", method = RequestMethod.POST)
    public RedirectView addFollower(@RequestParam long profileID, @RequestParam long principleID){

        ApplicationUser profileUser = appUserRepo.findById(profileID).get();
        ApplicationUser currentUser = appUserRepo.findById(principleID).get();

        profileUser.followerSet.add(currentUser);
        profileUser.userBeingFollowed.add(profileUser);

        System.out.println(profileUser.followerSet.size());

        return new RedirectView("/users/" + profileID + "/show");
    }
}
