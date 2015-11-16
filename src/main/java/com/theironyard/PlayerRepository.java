package com.theironyard;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Jack on 11/12/15.
 */
public interface PlayerRepository extends PagingAndSortingRepository<Player, Integer> {
}
