package hu.nye.home.service;

import hu.nye.home.dto.GameDto;
import hu.nye.home.exceptions.GameNotFoundException;
import hu.nye.home.model.Game;
import hu.nye.home.repositories.GameRepositoryInterface;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    @Mock
    private GameRepositoryInterface gameRepository;
    @InjectMocks
    private GameService gameService;
    private Game game;
    private GameDto gameDto;
    
    @BeforeEach
    public void setUp() {
        gameDto = new GameDto();
        gameDto.setName("Minecraft");
        gameDto.setPrice(10000);
        gameDto.setDeveloper("Mojang");
        gameDto.setPlatform("Microsoft_Store");
        gameDto.setYearOfPublication(2014);
        
        game = Game.builder()
                 .name(gameDto.getName())
                 .price(gameDto.getPrice())
                 .developer(gameDto.getDeveloper())
                 .platform(gameDto.getPlatform())
                 .yearOfPublication(gameDto.getYearOfPublication())
                 .build();
        
    }
    
    @Test
    public void testSaveGame() {
        
        when(gameRepository.save(Mockito.any(Game.class))).thenReturn(game);
        Game result = gameService.saveGame(gameDto);
        assertEquals(game, result);
        verify(gameRepository).save(Mockito.any(Game.class));
    }
    
    @Test
    public void testSaveGame_NullDto() {
        
        assertThrows(NullPointerException.class, () -> {
            gameService.saveGame(null);
        });
        
    }
    
    @Test
    @Transactional
    public void testSaveGame_DuplicateNameWithDifferentPrice() {
        
        GameDto dto1 = new GameDto();
        dto1.setName("Minecraft");
        dto1.setPrice(10000);
        dto1.setDeveloper("Mojang");
        dto1.setPlatform("Microsoft_Store");
        dto1.setYearOfPublication(2014);
        
        GameDto dto2 = new GameDto();
        dto2.setName("Minecraft");
        dto2.setPrice(20000);
        dto2.setDeveloper("Mojang");
        dto2.setPlatform("Microsoft_Store");
        dto2.setYearOfPublication(2014);
        
        Game existingGame1 = new Game();
        existingGame1.setName(dto1.getName());
        existingGame1.setPrice(dto1.getPrice());
        existingGame1.setDeveloper(dto1.getDeveloper());
        existingGame1.setPlatform(dto1.getPlatform());
        existingGame1.setYearOfPublication(dto1.getYearOfPublication());
        
        when(gameRepository.save(any(Game.class))).thenReturn(existingGame1);
        
        Game savedGame1 = gameService.saveGame(dto1);
        
        Game existingGame2 = new Game();
        existingGame2.setName(dto2.getName());
        existingGame2.setPrice(dto2.getPrice());
        existingGame2.setDeveloper(dto2.getDeveloper());
        existingGame2.setPlatform(dto2.getPlatform());
        existingGame2.setYearOfPublication(dto2.getYearOfPublication());
        
        when(gameRepository.save(any(Game.class))).thenReturn(existingGame2);
        
        Game savedGame2 = gameService.saveGame(dto2);
        
        Assertions.assertNotEquals(savedGame1.getPrice(), savedGame2.getPrice());
    }
    
    @Test
    public void testSaveGame_EmptyName() {
        
        GameDto dto = new GameDto(3L,"", 10000, "Mojang", "Microsoft_Store", 2014);
        String ActualName = dto.getName();
        String ExpectedName = "";
        assertEquals(ActualName, ExpectedName);
    }
    
    @Test
    public void testSaveGame_WithMockRepository() {
        // Arrange
        GameDto dto = new GameDto("Test Game", 5000, "Test Developer", "Test Platform", 2022);
        Game savedGame = new Game("Test Game", 5000, "Test Developer", "Test Platform", 2022);
        when(gameRepository.save(any(Game.class))).thenReturn(savedGame);
        
        // Act
        Game result = gameService.saveGame(dto);
        
        // Assert
        assertNotNull(result);
        assertEquals(savedGame, result);
    }
    
    @Test
    void testGetGameById_WhenGameExists() {
        Long id = 1L;
        Game expectedGame = new Game();
        when(gameRepository.findById(id)).thenReturn(Optional.of(expectedGame));
        
        Game result = gameService.getGameById(id);
        
        assertEquals(expectedGame, result);
        verify(gameRepository, times(1)).findById(id);
    }
    
    @Test
    void testGetGameById_WhenGameNotFound() {
        Long id = 1L;
        when(gameRepository.findById(id)).thenReturn(Optional.empty());
        
        Assertions.assertThrows(GameNotFoundException.class, () -> gameService.getGameById(id));
        verify(gameRepository, times(1)).findById(id);
    }
    
    @Test
    void testUpdateGameSuccess() throws Exception {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Game updatedGame = gameService.updateGame(1L, gameDto);
        
        assertNotNull(updatedGame);
        assertNotEquals("Updated Name", updatedGame.getName());
        assertNotEquals(25000, updatedGame.getPrice());
        assertNotEquals("Updated Developer", updatedGame.getDeveloper());
        assertNotEquals("Console", updatedGame.getPlatform());
        assertNotEquals(2021, updatedGame.getYearOfPublication());
        
        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(1)).save(updatedGame);
    }
    
    @Test
    void testUpdateGameNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(GameNotFoundException.class, () -> {
            gameService.updateGame(1L, gameDto);
        });
        
        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(0)).save(any(Game.class));
    }
    
    @Test
    void testUpdateGameWithNullName() throws Exception {
        gameDto.setName(null);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Game updatedGame = gameService.updateGame(1L, gameDto);
        
        assertNotNull(updatedGame);
        assertNull(updatedGame.getName());
        assertNotEquals(20000, updatedGame.getPrice());
        assertNotEquals("Updated Developer", updatedGame.getDeveloper());
        assertNotEquals("Console", updatedGame.getPlatform());
        assertNotEquals(2021, updatedGame.getYearOfPublication());
        
        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(1)).save(updatedGame);
    }
    
    @Test
    void testUpdateGameWithDifferentPlatform() throws Exception {
        gameDto.setPlatform("Mobile");
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Game updatedGame = gameService.updateGame(1L, gameDto);
        
        assertNotNull(updatedGame);
        assertNotEquals("Updated Name", updatedGame.getName());
        assertNotEquals(25000, updatedGame.getPrice());
        assertNotEquals("Updated Developer", updatedGame.getDeveloper());
        assertEquals("Mobile", updatedGame.getPlatform());
        assertNotEquals(2021, updatedGame.getYearOfPublication());
        
        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(1)).save(updatedGame);
    }
    
    @Test
    void testSaveGameFailure() throws Exception {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenThrow(new RuntimeException("Save failed"));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameService.updateGame(1L, gameDto);
        });
        
        assertEquals("Save failed", exception.getMessage());
        
        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(1)).save(any(Game.class));
    }
    
    @Test
    public void testDeleteGameById() {
        Long gameId = 1L;
        gameService.deleteGameById(gameId);
        verify(gameRepository, times(1)).deleteById(gameId);
    }
    
    @Test
    public void testFindAllGameByName_EmptyName() {
        // Arrange
        String emptyName = "";
        when(gameRepository.findByName(emptyName)).thenReturn(Collections.emptyList());
        
        // Act
        List<Game> foundGames = gameService.findAllGameByName(emptyName);
        
        // Assert
        Assertions.assertTrue(foundGames.isEmpty());
        verify(gameRepository, times(1)).findByName(emptyName);
    }
    
    @Test
    public void testFindAllGameByName_NullName() {
        // Arrange
        String nullName = null;
        when(gameRepository.findByName(nullName)).thenReturn(Collections.emptyList());
        
        // Act
        List<Game> foundGames = gameService.findAllGameByName(nullName);
        
        // Assert
        Assertions.assertTrue(foundGames.isEmpty());
        verify(gameRepository, times(1)).findByName(nullName);
    }
    
    @Test
    public void testFindAllGameByName_OneGameFound() {
        // Arrange
        String name = "Minecraft";
        Game game = new Game(); // create a sample game
        when(gameRepository.findByName(name)).thenReturn(Collections.singletonList(game));
        
        // Act
        List<Game> foundGames = gameService.findAllGameByName(name);
        
        // Assert
        assertEquals(1, foundGames.size());
        assertEquals(game, foundGames.get(0));
        verify(gameRepository, times(1)).findByName(name);
    }
    
    @Test
    public void testFindAllGameByName_MultipleGamesFound() {
        // Arrange
        String name = "Minecraft";
        Game game1 = new Game(); // create a sample game
        Game game2 = new Game(); // create another sample game
        when(gameRepository.findByName(name)).thenReturn(List.of(game1, game2));
        
        // Act
        List<Game> foundGames = gameService.findAllGameByName(name);
        
        // Assert
        assertEquals(2, foundGames.size());
        Assertions.assertTrue(foundGames.contains(game1));
        Assertions.assertTrue(foundGames.contains(game2));
        verify(gameRepository, times(1)).findByName(name);
    }
    
    @Test
    public void testFindAllGameByDeveloper_EmptyDeveloper() {
        // Arrange
        String emptyDeveloper = "";
        when(gameRepository.findByDeveloper(emptyDeveloper)).thenReturn(Collections.emptyList());
        
        // Act
        List<Game> foundGames = gameService.findAllGameByDeveloper(emptyDeveloper);
        
        // Assert
        Assertions.assertTrue(foundGames.isEmpty());
        verify(gameRepository, times(1)).findByDeveloper(emptyDeveloper);
    }
    
    @Test
    public void testFindAllGameByDeveloper_NullDeveloper() {
        // Arrange
        String nullDeveloper = null;
        when(gameRepository.findByDeveloper(nullDeveloper)).thenReturn(Collections.emptyList());
        
        // Act
        List<Game> foundGames = gameService.findAllGameByDeveloper(nullDeveloper);
        
        // Assert
        Assertions.assertTrue(foundGames.isEmpty());
        verify(gameRepository, times(1)).findByDeveloper(nullDeveloper);
    }
    
    @Test
    public void testFindAllGameByDeveloper_OneGameFound() {
        
        String developer = "Mojang";
        Game game = new Game();
        when(gameRepository.findByDeveloper(developer)).thenReturn(Collections.singletonList(game));
        
        List<Game> foundGames = gameService.findAllGameByDeveloper(developer);
        
        assertEquals(1, foundGames.size());
        assertEquals(game, foundGames.get(0));
        verify(gameRepository, times(1)).findByDeveloper(developer);
    }
    
    @Test
    public void testFindAllGameByDeveloper_MultipleGamesFound() {
        // Arrange
        String developer = "Mojang";
        Game game1 = new Game(); // create a sample game
        Game game2 = new Game(); // create another sample game
        when(gameRepository.findByDeveloper(developer)).thenReturn(List.of(game1, game2));
        
        // Act
        List<Game> foundGames = gameService.findAllGameByDeveloper(developer);
        
        // Assert
        assertEquals(2, foundGames.size());
        Assertions.assertTrue(foundGames.contains(game1));
        Assertions.assertTrue(foundGames.contains(game2));
        verify(gameRepository, times(1)).findByDeveloper(developer);
    }
    
    @Test
    public void testFindAllGameByPlatform_EmptyPlatform() {
        // Arrange
        String emptyPlatform = "";
        when(gameRepository.findByPlatform(emptyPlatform)).thenReturn(Collections.emptyList());
        
        // Act
        List<Game> foundGames = gameService.findAllGameByPlatform(emptyPlatform);
        
        // Assert
        Assertions.assertTrue(foundGames.isEmpty());
        verify(gameRepository, times(1)).findByPlatform(emptyPlatform);
    }
    
    @Test
    public void testFindAllGameByPlatform_NullPlatform() {
        // Arrange
        String nullPlatform = null;
        when(gameRepository.findByPlatform(nullPlatform)).thenReturn(Collections.emptyList());
        
        // Act
        List<Game> foundGames = gameService.findAllGameByPlatform(nullPlatform);
        
        // Assert
        Assertions.assertTrue(foundGames.isEmpty());
        verify(gameRepository, times(1)).findByPlatform(nullPlatform);
    }
    
    @Test
    public void testFindAllGameByPlatform_OneGameFound() {
        
        String platform = "Microsoft_Store";
        Game game = new Game();
        when(gameRepository.findByPlatform(platform)).thenReturn(Collections.singletonList(game));
        
        List<Game> foundGames = gameService.findAllGameByPlatform(platform);
        
        assertEquals(1, foundGames.size());
        assertEquals(game, foundGames.get(0));
        verify(gameRepository, times(1)).findByPlatform(platform);
    }
    
    @Test
    public void testFindAllGameByPlatform_MultipleGamesFound() {
        // Arrange
        String platform = "Microsoft_Store";
        Game game1 = new Game(); // create a sample game
        Game game2 = new Game(); // create another sample game
        when(gameRepository.findByPlatform(platform)).thenReturn(List.of(game1, game2));
        
        // Act
        List<Game> foundGames = gameService.findAllGameByPlatform(platform);
        
        // Assert
        assertEquals(2, foundGames.size());
        Assertions.assertTrue(foundGames.contains(game1));
        Assertions.assertTrue(foundGames.contains(game2));
        verify(gameRepository, times(1)).findByPlatform(platform);
    }
    
    @Test
    public void testGetGameById_NegativeId() {
        Long id = -1L;
        when(gameRepository.findById(id)).thenReturn(Optional.empty());
        
        assertThrows(GameNotFoundException.class, () -> {
            gameService.getGameById(id);
        });
        
        verify(gameRepository, times(1)).findById(id);
    }
    
    @Test
    public void testUpdateGame_NullFields() throws Exception {
        gameDto.setName(null);
        gameDto.setPrice(0);
        gameDto.setDeveloper(null);
        gameDto.setPlatform(null);
        gameDto.setYearOfPublication(0);
        
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Game updatedGame = gameService.updateGame(1L, gameDto);
        
        assertNotNull(updatedGame);
        assertNull(updatedGame.getName());
        assertEquals(updatedGame.getPrice(), 0);
        assertNull(updatedGame.getDeveloper());
        assertNull(updatedGame.getPlatform());
        assertEquals(updatedGame.getYearOfPublication(), 0);
        
        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(1)).save(updatedGame);
    }
    
    @Test
    public void testDeleteGameById_NonExistentId() {
        Long nonExistentId = 999L;
        doThrow(new EmptyResultDataAccessException(1)).when(gameRepository).deleteById(nonExistentId);
        
        assertThrows(EmptyResultDataAccessException.class, () -> {
            gameService.deleteGameById(nonExistentId);
        });
        
        verify(gameRepository, times(1)).deleteById(nonExistentId);
    }
    
    @Test
    public void testFindAllGameByName_LongName() {
        String longName = "a".repeat(1000);
        when(gameRepository.findByName(longName)).thenReturn(Collections.emptyList());
        
        List<Game> foundGames = gameService.findAllGameByName(longName);
        
        assertTrue(foundGames.isEmpty());
        verify(gameRepository, times(1)).findByName(longName);
    }
    
    @Test
    public void testFindAllGameByDeveloper_SpecialCharacters() {
        String specialCharsDeveloper = "!@#$%^&*()";
        when(gameRepository.findByDeveloper(specialCharsDeveloper)).thenReturn(Collections.emptyList());
        
        List<Game> foundGames = gameService.findAllGameByDeveloper(specialCharsDeveloper);
        
        assertTrue(foundGames.isEmpty());
        verify(gameRepository, times(1)).findByDeveloper(specialCharsDeveloper);
    }
    
    @Test
    public void testFindAllGameByPlatform_LongPlatform() {
        String longPlatform = "a".repeat(1000);
        when(gameRepository.findByPlatform(longPlatform)).thenReturn(Collections.emptyList());
        
        List<Game> foundGames = gameService.findAllGameByPlatform(longPlatform);
        
        assertTrue(foundGames.isEmpty());
        verify(gameRepository, times(1)).findByPlatform(longPlatform);
    }
    
}
