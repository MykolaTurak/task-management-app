package mate.academy.demo.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.swing.text.html.parser.Entity;
import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.user.CreateUserRequestDto;
import mate.academy.demo.dto.user.UserDto;
import mate.academy.demo.exception.AuthenticationException;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.mapper.UserMapper;
import mate.academy.demo.model.Role;
import mate.academy.demo.model.RoleName;
import mate.academy.demo.model.User;
import mate.academy.demo.repository.RoleRepository;
import mate.academy.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto create(CreateUserRequestDto userRequestDto) {
        Optional<User> byEmail = userRepository.findByEmail(userRequestDto.getEmail());
        if (byEmail.isPresent()) {
            throw new AuthenticationException("User with email: "
            + userRequestDto.getEmail() + "already exist");
        }
        User user = userMapper.toModel(userRequestDto);

        Role role = roleRepository.findByName(RoleName.USER)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Can't find role with name: " + RoleName.USER.name()
                        ));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }
}
