package ee.taltech.iti03022024backend.repositories.user;

import ee.taltech.iti03022024backend.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String user);
    Optional<UserEntity> findByUsername(String username);
}
