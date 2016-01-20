package com.theironyard.services;

import com.theironyard.entities.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Jack on 11/12/15.
 */
public interface PlayerRepository extends PagingAndSortingRepository<Player, Integer> {
    Player findOneByName(String name);

    @Query("SELECT p FROM Player p WHERE LOWER(name) LIKE '%' || LOWER(?) || '%'")
    List<Player> searchByName(String name);

    List<Player> findAllByTeam(String team);

    List<Player> findAllByPosition(String position);
}
