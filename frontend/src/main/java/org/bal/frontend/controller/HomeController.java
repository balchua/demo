package org.bal.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "http://demo.geek.per.sg/rightNow", maxAge = 3600)
public class HomeController {

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

}
