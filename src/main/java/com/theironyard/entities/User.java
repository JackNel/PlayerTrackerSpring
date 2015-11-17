package com.theironyard.entities;

import com.theironyard.entities.Player;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Jack on 11/12/15.
 */
@Entity
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column (nullable = false)
    int id;

    @Column (nullable = false)
    public String username;
    @Column (nullable = false)
    public String password;

    @OneToMany (mappedBy = "user")
    public List<Player> players;
}
