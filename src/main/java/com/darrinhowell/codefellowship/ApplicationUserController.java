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
import java.util.Set;

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

        if(profileUser.usersIAmFollowing == null) {
            m.addAttribute("usersIAmFollowing", 0);
        } else {
            m.addAttribute("usersIAmFollowing", profileUser.usersIAmFollowing.size());
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

        if(currentUser.usersIAmFollowing == null) {
            m.addAttribute("usersIAmFollowing", 0);
        } else {
            m.addAttribute("usersIAmFollowing", currentUser.usersIAmFollowing.size());
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
            ApplicationUser currentUser = appUserRepo.findById(userId).get();
            Post newPost = new Post(blogPostBody, new Date());
            newPost.user = currentUser;
            userPostRepo.save(newPost);
//            appUserRepo.save(currentUser);

            for(int i = 0; i < currentUser.postSet.size(); i++){
                System.out.println(currentUser.postSet.get(i).body);
            }

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

        currentUser.usersIAmFollowing.add(profileUser);

        appUserRepo.save(currentUser);

        return new RedirectView("/users/" + profileID + "/show");
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public String loadFeedOfUsersIAmFollowing(Principal p, Model m){

        ApplicationUser currentUser = appUserRepo.findByUsername(p.getName());

        Set<ApplicationUser> followeesSet = currentUser.usersIAmFollowing;

        ApplicationUser[] followeesArr = followeesSet.toArray(new ApplicationUser[followeesSet.size()]);

        m.addAttribute("followees", followeesArr);

        return "feed";
    }
}
