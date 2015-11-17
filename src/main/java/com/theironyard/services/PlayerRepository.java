package com.theironyard.services;

import com.theironyard.entities.Player;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Jack on 11/12/15.
 */
public interface PlayerRepository extends PagingAndSortingRepository<Player, Integer> {
    Player findOneByName(String name);
}
