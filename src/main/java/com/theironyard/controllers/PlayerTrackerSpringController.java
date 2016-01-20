package com.theironyard.controllers;

import com.theironyard.entities.Player;
import com.theironyard.entities.User;
import com.theironyard.services.PlayerRepository;
import com.theironyard.services.UserRepository;
import com.theironyard.util.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
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
    public String home(Model model,
                       HttpSession session,
                       @RequestParam(defaultValue = "0") int page,
                       String showMine,
                       String searchByName,
                       String searchByTeam,
                       String searchByPosition) {

        PageRequest pr = new PageRequest(page, 10);
        Page p;
        p = players.findAll(pr);

        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("nextPage", page+1);
        model.addAttribute("showNext", p.hasNext());
        model.addAttribute("prevPage", page-1);
        model.addAttribute("showPrev", p.hasPrevious());

        if (showMine != null) {
            model.addAttribute("players", users.findOneByUsername(username).players);
        }
        else if (searchByName != null) {
            model.addAttribute("players", players.searchByName(searchByName));
        }
        else if (searchByTeam != null) {
            model.addAttribute("players", players.findAllByTeam(searchByTeam));
        }
        else if (searchByPosition != null) {
            model.addAttribute("players", players.findAllByPosition(searchByPosition));
        }
        else {
            model.addAttribute("players", p);
        }
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
    public String create(HttpSession session, String name, int number, String team, String position, int age) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in");
        }
        User user = users.findOneByUsername(username);
        Player player = new Player();
        player.name = name;
        player.number = number;
        player.team = team;
        player.position = position;
        player.age = age;
        player.user = user;
        user.players.add(player);
        players.save(player);
        return "redirect:/";
    }

    @RequestMapping("/edit")
    public String edit(HttpSession session, Model model, int id) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in");
        }
        Player player = players.findOne(id);
        model.addAttribute("player", player);
        return "edit";
    }

    @RequestMapping("/edit-player")
    public String editPlayer(HttpSession session, int id, String name, int number, String team, String position, int age) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in");
        }
        Player player = players.findOne(id);
        player.name = name;
        player.number = number;
        player.team = team;
        player.position = position;
        player.age = age;
        players.save(player);
        return "redirect:/";
    }

    @RequestMapping("/delete")
    public String delete(HttpSession session, int id) throws Exception {
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
