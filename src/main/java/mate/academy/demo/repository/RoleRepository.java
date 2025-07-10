package mate.academy.demo.repository;

import java.util.Optional;
import mate.academy.demo.model.Role;
import mate.academy.demo.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
