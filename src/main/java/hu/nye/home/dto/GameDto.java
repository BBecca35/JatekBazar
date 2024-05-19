package hu.nye.home.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Game.
 */
@SuppressWarnings("checkstyle:Indentation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameDto {
    
    private Long id;
    
    @NotEmpty
    @NotNull
    private String name;
    
    @Positive
    private int price;
    
    @NotEmpty
    @NotNull
    private String developer;
    
    @NotEmpty
    @NotNull
    private String platform;
    
    @Positive
    private int yearOfPublication;
    
    /**
     * This is a Constructor without id field.
     */
    public GameDto(String name, int price, String developer,
                   String platform, int yearOfPublication) {
        this.name = name;
        this.price = price;
        this.developer = developer;
        this.platform = platform;
        this.yearOfPublication = yearOfPublication;
    }
}


