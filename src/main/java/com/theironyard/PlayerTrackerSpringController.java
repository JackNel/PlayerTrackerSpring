package com.theironyard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by Jack on 11/12/15.
 */

@Controller
public class PlayerTrackerSpringController {
    @Autowired
    UserRepository users;
    @Autowired
    PlayerRepository players;

    @RequestMapping("/")
    public String home(Model model, HttpSession session) {

        return "home";
    }
}
