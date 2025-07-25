package mate.academy.demo.repository;

import static mate.academy.demo.util.TestUtil.getAdminRole;
import static org.junit.Assert.assertEquals;

import java.util.Optional;
import mate.academy.demo.config.CustomMySqlContainer;
import mate.academy.demo.model.Role;
import mate.academy.demo.model.RoleName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {
    @Container
    private static MySQLContainer<?> mysql = CustomMySqlContainer.getInstance()
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("testdb");

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("""
            Find role by it's name
            """)
    void findByName_WithValidRoleName_ShouldReturnValidRole() {
        Optional<Role> expected = Optional.of(getAdminRole());

        Optional<Role> actual = roleRepository.findByName(RoleName.ADMIN);

        assertEquals(expected, actual);
    }
}
