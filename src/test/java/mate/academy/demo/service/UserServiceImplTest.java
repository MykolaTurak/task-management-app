package mate.academy.demo.service;

import static mate.academy.demo.util.TestUtil.getAdminRole;
import static mate.academy.demo.util.TestUtil.getFirstUser;
import static mate.academy.demo.util.TestUtil.getUpdateAdminRoleRequestDto;
import static mate.academy.demo.util.TestUtil.getUpdateUserRoleRequestDto;
import static mate.academy.demo.util.TestUtil.getUserDto;
import static mate.academy.demo.util.TestUtil.getUserRequestDto;
import static mate.academy.demo.util.TestUtil.getUserRole;
import static mate.academy.demo.util.TestUtil.getUserWithRolesDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.demo.dto.user.UserDto;
import mate.academy.demo.dto.user.UserWithRolesDto;
import mate.academy.demo.exception.AuthenticationException;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.exception.RequestDataException;
import mate.academy.demo.mapper.UserMapper;
import mate.academy.demo.model.RoleName;
import mate.academy.demo.model.User;
import mate.academy.demo.repository.RoleRepository;
import mate.academy.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("""
            create user with valid request data
            """)
    void create_WithValidData_ShouldReturnValidDto() {
        UserDto expected = getUserDto();

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty()
                );
        when(userMapper
                .toModel(getUserRequestDto())).thenReturn(getFirstUser());
        when(roleRepository.findByName(any(RoleName.class)))
                .thenReturn((Optional.of(getUserRole())));
        when(passwordEncoder.encode(anyString()))
                .thenReturn(getFirstUser().getPassword());
        when(userRepository.save(any(User.class)))
                .thenReturn(getFirstUser());
        when(userMapper.toDto(getFirstUser()))
                .thenReturn(expected);

        UserDto actual = userService.create(getUserRequestDto());

        assertEquals(expected, actual);

        verify(userRepository).findByEmail(anyString());
        verify(userMapper).toModel(getUserRequestDto());
        verify(roleRepository).findByName(any(RoleName.class));
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toDto(getFirstUser());
    }

    @Test
    @DisplayName("""
            create user with already exist email
            """)
    void create_WithAlreadyExistEmail_ShouldThrowException() {
        String expectedMessage = "User with email: "
                + getUserRequestDto().getEmail() + "already exist";

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(getFirstUser()));

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                () -> userService.create(getUserRequestDto()));

        assertEquals(expectedMessage, actual.getMessage());

        verify(userRepository).findByEmail(anyString());
    }

    @Test
    @DisplayName("""
            create user with non exist role
            """)
    void create_WithNonExistRole_ShouldThrowException() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        when(userMapper
                .toModel(getUserRequestDto())).thenReturn(getFirstUser());
        when(roleRepository.findByName(any(RoleName.class)))
                .thenReturn((Optional.empty()));

        String expectedMessage = "Can't find role with name: " + RoleName.USER.name();

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> userService.create(getUserRequestDto()));

        assertEquals(expectedMessage, actual.getMessage());

        verify(userRepository).findByEmail(anyString());
        verify(userMapper).toModel(getUserRequestDto());
        verify(roleRepository).findByName(any(RoleName.class));
    }

    @Test
    @DisplayName("""
            check if user exists by id
            """)
    void existById_withValidId_ShouldReturnTrue() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        boolean actual = userService.existById(anyLong());

        assertTrue(actual);

        verify(userRepository).existsById(anyLong());
    }

    @Test
    @DisplayName("""
            get current existing user
            """)
    void getCurrentUser_WithExistUser_ShouldReturnValidUser() {
        UserDto expected = getUserDto();

        when(authenticationService.getCurrentUserId()).thenReturn(expected.getId());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(getFirstUser()));
        when(userMapper.toDto(getFirstUser())).thenReturn(expected);

        UserDto actual = userService.getCurrentUser();

        assertEquals(expected, actual);

        verify(authenticationService).getCurrentUserId();
        verify(userRepository).findById(anyLong());
        verify(userMapper).toDto(getFirstUser());
    }

    @Test
    @DisplayName("""
            get current non existing user
            """)
    void getCurrentUser_WithNonExistUser_ShouldThrowException() {
        Long userId = 1L;
        String expectedMessage = "Can't find user with id: " + userId;

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> userService.getCurrentUser());

        assertEquals(expectedMessage, actual.getMessage());

        verify(authenticationService, times(2)).getCurrentUserId();
        verify(userRepository).findById(anyLong());
    }

    @Test
    @DisplayName("""
            update user with valid data
            """)
    void updateCurrentUser_WithValidData_ShouldReturnValidDto() {
        UserDto expected = getUserDto();
        Long userId = expected.getId();

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(getFirstUser()));
        when(userRepository.save(getFirstUser())).thenReturn(getFirstUser());
        when(userMapper.toDto(getFirstUser())).thenReturn(expected);

        UserDto actual = userService.updateCurrentUser(getUserRequestDto());

        assertEquals(expected, actual);

        verify(authenticationService).getCurrentUserId();
        verify(userMapper).toDto(getFirstUser());
        verify(userMapper).updateModelFromDto(getUserRequestDto(), getFirstUser());
        verify(userRepository).findById(anyLong());
        verify(userRepository).save(getFirstUser());

    }

    @Test
    @DisplayName("""
            update user with valid data
            """)
    void updateCurrentUser_WithNonExistUser_ShouldThrowException() {
        Long userId = 1L;
        String expectedMessage = "Can't find user with id: " + userId;

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> userService.updateCurrentUser(getUserRequestDto()));

        assertEquals(expectedMessage, actual.getMessage());

        verify(authenticationService, times(2)).getCurrentUserId();
        verify(userRepository).findById(anyLong());
    }

    @Test
    @DisplayName("""
            change user role with valid request
            """)
    void changeUserRole_WithValidData_ShouldReturnValidDto() {
        UserWithRolesDto expected = getUserWithRolesDto();
        Long userId = expected.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(getFirstUser()));
        when(roleRepository.findByName(getUpdateAdminRoleRequestDto().getRoleName()))
                .thenReturn(Optional.of(getAdminRole()));
        when(userRepository.save(any(User.class))).thenReturn(getFirstUser());
        when(userMapper.toDtoWithRoles(getFirstUser())).thenReturn(expected);

        UserWithRolesDto actual = userService
                .changeUserRole(userId, getUpdateAdminRoleRequestDto());

        assertEquals(expected, actual);

        verify(userRepository).findById(userId);
        verify(roleRepository).findByName(getUpdateAdminRoleRequestDto().getRoleName());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toDtoWithRoles(getFirstUser());
    }

    @Test
    @DisplayName("""
            change user role with invalid user id
            """)
    void changeUserRole_WithInvalidId_ShouldTrowException() {
        Long userId = 1L;
        String expectedMessage = "Can't find user with id: " + userId;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> userService.changeUserRole(userId, getUpdateAdminRoleRequestDto()));

        assertEquals(expectedMessage, actual.getMessage());

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("""
            change user role with invalid role name
            """)
    void changeUserRole_WithInvalidRole_ShouldTrowException() {
        Long userId = 1L;
        String expectedMessage = "Can't find role with name: "
                + getUpdateAdminRoleRequestDto().getRoleName();
        when(userRepository.findById(userId)).thenReturn(Optional.of(getFirstUser()));
        when(roleRepository.findByName(getUpdateAdminRoleRequestDto().getRoleName()))
                .thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> userService.changeUserRole(userId, getUpdateAdminRoleRequestDto()));

        assertEquals(expectedMessage, actual.getMessage());

        verify(userRepository).findById(userId);
        verify(roleRepository).findByName(getUpdateAdminRoleRequestDto().getRoleName());
    }

    @Test
    @DisplayName("""
            change user role with already exist role
            """)
    void changeUserRole_WithAlreadyExistRole_ShouldTrowException() {
        Long userId = 1L;
        String expectedMessage = "User with id: " + userId + " already has role: "
                + RoleName.USER;

        when(userRepository.findById(userId)).thenReturn(Optional.of(getFirstUser()));
        when(roleRepository.findByName(getUpdateUserRoleRequestDto().getRoleName()))
                .thenReturn(Optional.of(getUserRole()));

        RequestDataException actual = assertThrows(RequestDataException.class,
                () -> userService.changeUserRole(userId, getUpdateUserRoleRequestDto()));

        assertEquals(expectedMessage, actual.getMessage());

        verify(userRepository).findById(userId);
        verify(roleRepository).findByName(getUpdateUserRoleRequestDto().getRoleName());
    }

    @Test
    @DisplayName("""
            find user by id
            """)
    void findById_WithValidId_ShouldReturnValidDto() {
        UserDto expected = getUserDto();

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(getFirstUser()));
        when(userMapper.toDto(getFirstUser())).thenReturn(expected);

        UserDto actual = userService.findById(anyLong());

        assertEquals(expected, actual);

        verify(userRepository).findById(anyLong());
        verify(userMapper).toDto(getFirstUser());
    }

    @Test
    @DisplayName("""
            find user by non exist id
            """)
    void findById_WithNonExistId_ShouldReturnValidDto() {
        Long userId = 1L;
        String expectedMessage = "Can't find user with id: " + userId;

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> userService.findById(userId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(userRepository).findById(anyLong());
    }
}
