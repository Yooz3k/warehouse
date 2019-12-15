package pg.ium.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pg.ium.warehouse.domain.Tyre;

@Repository
public interface TyreRepository extends JpaRepository<Tyre, Long> {
}
