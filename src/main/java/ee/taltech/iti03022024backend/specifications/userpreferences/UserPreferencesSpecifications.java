package ee.taltech.iti03022024backend.specifications.userpreferences;

import ee.taltech.iti03022024backend.entities.usepreferences.UserPreferencesEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserPreferencesSpecifications {

    private UserPreferencesSpecifications() {}

    public static Specification<UserPreferencesEntity> getByUserId(Long userId) {
        return (root, query, cb) ->
                userId == null ? null : cb.equal(root.get("userId"), userId);
    }
}
