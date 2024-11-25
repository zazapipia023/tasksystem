package zazadev.tasksystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import zazadev.tasksystem.dto.UserDto;
import zazadev.tasksystem.enums.Role;
import zazadev.tasksystem.model.User;
import zazadev.tasksystem.repositories.UserRepository;
import zazadev.tasksystem.services.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetUserById() {
        ModelMapper modelMapper = new ModelMapper();
        UserService userService = new UserService(userRepository, modelMapper);

        User user = new User(1L, "user@example.com", "password", Role.USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    public void testCreateUser() {
        ModelMapper modelMapper = new ModelMapper();
        UserService userService = new UserService(userRepository, modelMapper);

        User user = new User(null, "user@example.com", "password", Role.USER);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userDto = new UserDto(null, "user@example.com", null);
        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
    }
}

