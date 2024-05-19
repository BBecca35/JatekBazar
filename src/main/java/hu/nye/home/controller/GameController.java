package hu.nye.home.controller;

import hu.nye.home.dto.GameDto;
import hu.nye.home.model.Game;
import hu.nye.home.service.GameServiceInterface;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST controller for managing games.
 */
@SuppressWarnings("checkstyle:Indentation")
@RestController
public class GameController {

    private final GameServiceInterface gameService;

    /**
     * Constructs a new GameController with the specified GameServiceInterface.
     *
     * @param gameService the game service to be used by this controller
     */
    @Autowired
    public GameController(GameServiceInterface gameService) {
        this.gameService = gameService;
    }

    /**
     * Adds a new game.
     *
     * @param dto the data transfer object containing game details
     * @return the saved game
     */
    @PostMapping("/games")
    public Game addNewGame(@RequestBody @Valid GameDto dto) {
        return gameService.saveGame(dto);
    }

    /**
     * Retrieves a game by its ID.
     *
     * @param id the ID of the game to be retrieved
     * @return the game with the specified ID
     */
    @GetMapping("/games/{id}")
    public Game getGameById(@PathVariable("id") Long id) {
        return gameService.getGameById(id);
    }

    /**
     * Updates an existing game.
     *
     * @param id the ID of the game to be updated
     * @param dto the data transfer object containing updated game details
     * @return the updated game
     */
    @PutMapping("/games/{id}")
    public Game updateGame(@PathVariable("id") Long id, @RequestBody @Valid GameDto dto) {
        return gameService.updateGame(id, dto);
    }

    /**
     * Deletes a game by its ID.
     *
     * @param id the ID of the game to be deleted
     */
    @DeleteMapping("/games/{id}")
    public void deleteGame(@PathVariable("id") Long id) {
        gameService.deleteGameById(id);
    }

    /**
     * Finds all games with the specified name.
     *
     * @param name the name of the games to be retrieved
     * @return a list of games with the specified name
     */
    @GetMapping("/games/name/{name}")
    public List<Game> findAllGameByName(@PathVariable("name") String name) {
        return gameService.findAllGameByName(name);
    }

    /**
     * Finds all games developed by the specified developer.
     *
     * @param developer the developer of the games to be retrieved
     * @return a list of games developed by the specified developer
     */
    @GetMapping("/games/developer/{developer}")
    public List<Game> findAllGameByDeveloper(@PathVariable("developer") String developer) {
        return gameService.findAllGameByDeveloper(developer);
    }

    /**
     * Finds all games for the specified platform.
     *
     * @param platform the platform of the games to be retrieved
     * @return a list of games for the specified platform
     */
    @GetMapping("/games/platform/{platform}")
    public List<Game> findAllGameByPlatform(@PathVariable("platform") String platform) {
        return gameService.findAllGameByPlatform(platform);
    }
}


