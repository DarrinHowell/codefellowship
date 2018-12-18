package com.darrinhowell.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ApplicationUserController {
    @Autowired
    public ApplicationUserRepository appUserRepo;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showSplash(){
        return "index";
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.GET)
    public String showSignUp() { return "sign-up"; }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public RedirectView addUsers(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String dateOfBirth,
                                 @RequestParam String bio) {
        ApplicationUser newUser = new ApplicationUser(username, password, firstName, lastName, dateOfBirth, bio);
        appUserRepo.save(newUser);
        return new RedirectView("/sign-up");
    }

}
