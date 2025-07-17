package mate.academy.demo.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.role.UpdateRoleRequestDto;
import mate.academy.demo.dto.user.CreateUserRequestDto;
import mate.academy.demo.dto.user.UserDto;
import mate.academy.demo.dto.user.UserWithRolesDto;
import mate.academy.demo.exception.AuthenticationException;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.exception.RequestDataException;
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
    private final AuthenticationService authenticationService;

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

    @Override
    public boolean existById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserDto getCurrentUser() {
        User user = userRepository.findById(authenticationService.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user with id: "
                                + authenticationService.getCurrentUserId()
                ));

        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateCurrentUser(CreateUserRequestDto requestDto) {
        User user = userRepository.findById(authenticationService.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user with id: "
                                + authenticationService.getCurrentUserId()
                ));

        userMapper.updateModelFromDto(requestDto, user);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserWithRolesDto changeUserRole(Long userId, UpdateRoleRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user with id: " + userId
                ));

        Role role = roleRepository.findByName(requestDto.getRoleName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find role with name: " + RoleName.USER.name()
                ));

        if (user.getRoles().contains(role)) {
            throw new RequestDataException(
                    "User with id: " + userId + " already has role: "
                            + requestDto.getRoleName().name()
            );
        }

        user.getRoles().add(role);

        return userMapper.toDtoWithRoles(userRepository.save(user));
    }

    @Override
    public UserDto findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user with id: " + userId
                ));
        
        return userMapper.toDto(user);
    }
}
