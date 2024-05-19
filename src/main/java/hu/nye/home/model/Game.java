package hu.nye.home.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a game.
 */
@SuppressWarnings("checkstyle:Indentation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int price;
    private String developer;
    private String platform;
    private int yearOfPublication;
    
    /**
     * This is a Constructor without id field.
     */
    public Game(String name, int price, String developer,
                   String platform, int yearOfPublication) {
        this.name = name;
        this.price = price;
        this.developer = developer;
        this.platform = platform;
        this.yearOfPublication = yearOfPublication;
    }
}
