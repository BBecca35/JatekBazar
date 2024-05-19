package hu.nye.home.service;

import hu.nye.home.dto.GameDto;
import hu.nye.home.exceptions.GameNotFoundException;
import hu.nye.home.model.Game;
import hu.nye.home.repositories.GameRepositoryInterface;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * A service class for managing games.
 */
@SuppressWarnings("checkstyle:Indentation")
@Service
public class GameService implements GameServiceInterface {
    
    private final GameRepositoryInterface gameRepository;
    
    @Autowired
    public GameService(GameRepositoryInterface gameRepository) {
        this.gameRepository = gameRepository;
    }
    
    
    @SuppressWarnings({"checkstyle:RightCurly", "checkstyle:WhitespaceAround",
      "checkstyle:WhitespaceAfter"})
    @Override
    public Game saveGame(GameDto dto) {
        Game game = new Game();
        if (dto == null) {
            throw new NullPointerException("GameDto cannot be null");
        }
        else{
            game.setName(dto.getName());
            game.setPrice(dto.getPrice());
            game.setDeveloper(dto.getDeveloper());
            game.setPlatform(dto.getPlatform());
            game.setYearOfPublication(dto.getYearOfPublication());
            gameRepository.save(game);
        }
        return game;
    }
    
    @Override
    @SneakyThrows
    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElseThrow(GameNotFoundException::new);
    }
    
    
    @Override
    @SneakyThrows
    public Game updateGame(Long id, GameDto dto) {
        Game game = gameRepository.findById(id).orElseThrow(GameNotFoundException::new);
        game.setName(dto.getName());
        game.setPrice(dto.getPrice());
        game.setDeveloper(dto.getDeveloper());
        game.setPlatform(dto.getPlatform());
        game.setYearOfPublication(dto.getYearOfPublication());
        return gameRepository.save(game);
    }
    
    @Override
    public void deleteGameById(Long id) {
        gameRepository.deleteById(id);
    }
    
    @Override
    public List<Game> findAllGameByName(String name) {
        return gameRepository.findByName(name);
    }
    
    @Override
    public List<Game> findAllGameByDeveloper(String developer) {
        return gameRepository.findByDeveloper(developer);
    }
    
    @Override
    public List<Game> findAllGameByPlatform(String platform) {
        return gameRepository.findByPlatform(platform);
    }
}
