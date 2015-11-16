package com.theironyard;

import sun.awt.image.URLImageSource;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.awt.image.BufferedImage;

/**
 * Created by Jack on 11/12/15.
 */
@Entity
public class Player {
    @Id
    @GeneratedValue
    Integer id;

    String name;
    Integer number;
    String team;
    String position;
    Integer age;
   // URLImageSource image;

    @ManyToOne
    User user;

}
