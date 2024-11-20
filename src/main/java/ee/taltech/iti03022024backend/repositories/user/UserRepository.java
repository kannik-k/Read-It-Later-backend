package ee.taltech.iti03022024backend.repositories.user;

import ee.taltech.iti03022024backend.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String user);
    UserEntity findByUsername(String username);
}
