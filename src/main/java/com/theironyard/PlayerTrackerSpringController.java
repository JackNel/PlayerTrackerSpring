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
    PlayerRepository players;
    @Autowired
    UserRepository users;

    @RequestMapping("/")
    public String home(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("players", players.findAll());
        return "home";
    }

    @RequestMapping("/login")
    public String login(HttpSession session, String username, String password) throws Exception {
        session.setAttribute("username", username);
        User user = users.findOneByUsername(username);
        if (user == null) {
            user = new User();
            user.username = username;
            user.password = PasswordHash.createHash(password);
            users.save(user);
        }
        else if (!PasswordHash.validatePassword(password, user.password)) {
            throw new Exception("Wrong password, try again!");
        }
        else if (username == null || password == null) {
            throw new Exception("Please enter both a username and password!");
        }
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/create")
    public String create(HttpSession session, String name, String team, String position, Integer age) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in");
        }
        User user = users.findOneByUsername(username);
        Player player = new Player();
        player.name = name;
        player.team = team;
        player.position = position;
        player.age = age;
        player.user = user;
        players.save(player);
        return "redirect:/";
    }

    @RequestMapping("/edit")
    public String edit(Model model, Integer id) {
        Player player = players.findOne(id);
        model.addAttribute("player", player);
        return "edit";
    }
}
