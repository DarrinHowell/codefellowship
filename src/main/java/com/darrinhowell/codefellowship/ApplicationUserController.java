package com.darrinhowell.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ApplicationUserController {
    @Autowired
    public ApplicationUserRepository appUserRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showSplash(){
        return "index";
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.GET)
    public String showSignUp() { return "sign-up"; }

    @RequestMapping(value = "/profiles", method = RequestMethod.GET)
    public String getProfiles(Model m){
        m.addAttribute("profiles", appUserRepo.findAll());
        return "profiles";
    }

    @RequestMapping(value = "/users/{profileId}/show", method = RequestMethod.GET)
    public String getProfiles(@PathVariable long profileId, Model m){
        m.addAttribute("profiles", appUserRepo.findById(profileId));
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

        appUserRepo.save(newUser);
        return new RedirectView("/sign-up");
    }

}
