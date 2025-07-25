package mate.academy.demo.service;

import static mate.academy.demo.util.TestUtil.getFirstUser;
import static mate.academy.demo.util.TestUtil.getValidAuthDto;
import static mate.academy.demo.util.TestUtil.getValidAuthRequestDto;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import mate.academy.demo.dto.authentication.AuthenticationDto;
import mate.academy.demo.security.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("""
            authenticate with valid data
            """)
    void authenticate_WithValidData_ShouldReturnValidDto() {
        AuthenticationDto expected = getValidAuthDto();

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                getValidAuthRequestDto().getEmail(), getValidAuthRequestDto().getPassword());

        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(getValidAuthRequestDto().getEmail());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken(getValidAuthRequestDto().getEmail()))
                .thenReturn(expected.token());

        authenticationService.authenticate(getValidAuthRequestDto());

        verify(authentication).getName();
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(getValidAuthRequestDto().getEmail());
    }

    @Test
    @DisplayName("getCurrentUserId should return ID from SecurityContext")
    void getCurrentUserId_ShouldReturnIdFromSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(getFirstUser());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        Long expected = getFirstUser().getId();

        Long actual = authenticationService.getCurrentUserId();

        assertEquals(expected, actual);
    }
}
