package ee.taltech.iti03022024backend.specifications.userpreferences;

import ee.taltech.iti03022024backend.entities.userPreferences.UserPreferencesEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserPreferencesSpecifications {

    private UserPreferencesSpecifications() {}

    public static Specification<UserPreferencesEntity> getByUserId(Long userId) {
        return (root, query, cb) ->
                userId == null ? null : cb.equal(root.get("userId"), userId);
    }

    public static Specification<UserPreferencesEntity> getByUserIdAndBookId(Long userId, Long genreId) {
        return (root, query, cb) -> {
            if (userId == null || genreId == null) return null;
            Predicate usrePredicate = cb.equal(root.get("userId"), userId);
            Predicate genrePredicate = cb.equal(root.get("genreId"), genreId);
            return cb.and(usrePredicate, genrePredicate);
        };
    }
}
