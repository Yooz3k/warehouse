package pg.ium.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pg.ium.warehouse.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

}