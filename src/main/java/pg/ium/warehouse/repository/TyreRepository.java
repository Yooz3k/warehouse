package pg.ium.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pg.ium.warehouse.domain.Tyre;

public interface TyreRepository extends JpaRepository<Tyre, Long> {
}
