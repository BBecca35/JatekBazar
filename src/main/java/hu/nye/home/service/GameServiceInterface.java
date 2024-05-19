package hu.nye.home.service;


import hu.nye.home.dto.GameDto;
import hu.nye.home.exceptions.GameNotFoundException;
import hu.nye.home.model.Game;
import java.util.List;

/**
 * Interface for managing Game entities.
 */
@SuppressWarnings("checkstyle:Indentation")
public interface GameServiceInterface {
    Game saveGame(GameDto dto);
    
    Game getGameById(Long id);
    
    Game updateGame(Long id, GameDto dto);
    
    void deleteGameById(Long id);
    
    List<Game> findAllGameByName(String name);
    
    List<Game> findAllGameByDeveloper(String developer);
    
    List<Game> findAllGameByPlatform(String platform);


}
