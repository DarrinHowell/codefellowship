package com.darrinhowell.codefellowship;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ApplicationUserController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showSplash(){
        return "index";
    }
}
