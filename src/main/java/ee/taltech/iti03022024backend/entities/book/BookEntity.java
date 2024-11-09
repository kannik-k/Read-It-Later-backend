package ee.taltech.iti03022024backend.entities.book;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;
    @Column
    private String title;
    @Column
    private String author;
    @Column
    private long genreId;
}
