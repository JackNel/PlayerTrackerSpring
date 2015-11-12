package com.theironyard;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Jack on 11/12/15.
 */
@Entity
public class Player {
    @Id
    @GeneratedValue
    Integer id;

    String name;
    String team;
    String position;
    Integer age;

    @ManyToOne
    User user;

}
