package hu.nye.home.repositories;

import hu.nye.home.model.Game;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for Game entity.
 */
@SuppressWarnings("checkstyle:Indentation")
@Repository
public interface GameRepositoryInterface extends CrudRepository<Game, Long> {
    
    /**
     * Finds games by name.
     *
     * @param name the name of the game
     * @return the list of games with the specified name
     */
    List<Game> findByName(String name);
    
    /**
     * Finds games by platform.
     *
     * @param platform the platform of the game
     * @return the list of games with the specified platform
     */
    List<Game> findByPlatform(String platform);
    
    /**
     * Finds games by developer.
     *
     * @param developer the developer of the game
     * @return the list of games with the specified developer
     */
    List<Game> findByDeveloper(String developer);
}
