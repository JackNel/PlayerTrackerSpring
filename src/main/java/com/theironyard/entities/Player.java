package com.theironyard.entities;

import javax.persistence.*;

/**
 * Created by Jack on 11/12/15.
 */
@Entity
public class Player {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    int id;

    @Column (nullable = false)
    public String name;
    @Column (nullable = false)
    public int number;
    @Column (nullable = false)
    public String team;
    @Column (nullable = false)
    public String position;
    @Column (nullable = false)
    public int age;
   // URLImageSource image;

    @ManyToOne
    public User user;

}
