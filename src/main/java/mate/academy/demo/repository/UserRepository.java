package mate.academy.demo.repository;

import java.util.Optional;
import mate.academy.demo.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findById(Long id);

    @Query("FROM User u WHERE u.temporaryToken = :token")
    Optional<User> findByTemporaryToken(@Param("token") String token);
}
