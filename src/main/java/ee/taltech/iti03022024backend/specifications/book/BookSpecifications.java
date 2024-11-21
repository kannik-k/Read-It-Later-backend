package ee.taltech.iti03022024backend.specifications.book;

import ee.taltech.iti03022024backend.entities.book.BookEntity;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecifications {

    // SonarLint asked for it
    private BookSpecifications() {}

    public static Specification<BookEntity> getByAuthor(String author) {
        return (root, query, criteriaBuilder) ->
                author == null ? null : criteriaBuilder.like(criteriaBuilder
                        .lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }

    public static Specification<BookEntity> getByTitle(String title) {
        return (root, query, criteriaBuilder) ->
                title == null ? null : criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<BookEntity> getByGenreId(Long genreId) {
        return (root, query, criteriaBuilder) ->
                genreId == null ? null : criteriaBuilder.equal(root.get("genreId"), genreId);
    }
}
