package com.theironyard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.awt.image.URLImageSource;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


/**
 * Created by Jack on 11/12/15.
 */

@Controller
public class PlayerTrackerSpringController {
    @Autowired
    PlayerRepository players;
    @Autowired
    UserRepository users;

    @PostConstruct
    public void init() throws InvalidKeySpecException, NoSuchAlgorithmException {
        User admin = users.findOneByUsername("Admin");
        if (admin == null) {
            admin = new User();
            admin.username = "Admin";
            admin.password = PasswordHash.createHash("Admin");
            users.save(admin);
        }
        if (players.count() == 0) {
            String fileContent = readFile("playerList.csv");
            String[] lines = fileContent.split("\n");
            for (String line : lines) {
                if (line == lines[0]) {
                    continue;
                }
                String[] columns = line.split(",");
                Player player = new Player();
                player.number = Integer.valueOf(columns[0]);
                player.name = columns[1];
                player.age = Integer.valueOf(columns[2]);
                player.position = columns[3];
                player.team = columns[4];
                player.user = admin;
                players.save(player);
            }
        }
    }

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
    public String create(HttpSession session, String name, String team, String position, Integer age, URLImageSource image) throws Exception {
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
       // player.image = image;
        players.save(player);
        return "redirect:/";
    }

    @RequestMapping("/edit")
    public String edit(HttpSession session, Model model, Integer id) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in");
        }
        Player player = players.findOne(id);
        model.addAttribute("player", player);
        return "edit";
    }

    @RequestMapping("/edit-player")
    public String editPlayer(HttpSession session, Integer id, String name, String team, String position, Integer age) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in");
        }
        Player player = players.findOne(id);
        player.name = name;
        player.team = team;
        player.position = position;
        player.age = age;
        players.save(player);
        return "redirect:/";
    }

    @RequestMapping("/delete")
    public String delete(HttpSession session, Integer id) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in");
        }
        players.delete(id);
        return "redirect:/";
    }

    static String readFile(String fileName) {
        File f = new File(fileName);
        try {
            FileReader fr = new FileReader(f);
            int fileSize = (int) f.length();
            char[] fileContent = new char[fileSize];
            fr.read(fileContent);
            return new String(fileContent);
        } catch (Exception e) {
            return null;
        }
    }
}
