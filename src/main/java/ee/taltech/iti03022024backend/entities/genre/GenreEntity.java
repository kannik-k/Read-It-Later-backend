package ee.taltech.iti03022024backend.entities.genre;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "\"genre\"")
public class GenreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long genreId;
    @Column
    private String genre;
}
