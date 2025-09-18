package com.example.Project_VuDucTrung.service;


import com.example.Project_VuDucTrung.model.User;
import com.example.Project_VuDucTrung.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void login_success_with_correct_information() {
        User user = new User(
                1L,
                "trungvu",
                "123456",
                "trungvu@samsung.com",
                "Vu Duc Trung",
                "123 Đường A, Hà Nội",
                "0335654203",
                "USER"
        );
        String username = "trungvu";
        String password = "123456";

        when(userRepository.findByUserNameAndPassWord(username, password))
                .thenReturn(user);

        User result = userService.login(username, password);

        assertThat(result, notNullValue());
    }

    @Test
    void login_fail_when_user_not_found() {
        String username = "nouser";
        String password = "123456";

        when(userRepository.findByUserNameAndPassWord(username, password))
                .thenReturn(null);

        User result = userService.login(username, password);

        assertNull(result);
    }

    @Test
    void login_fail_with_wrong_password() {
        String username = "trungvu";
        String password = "wrongpass";

        when(userRepository.findByUserNameAndPassWord(username, password))
                .thenReturn(null);

        User result = userService.login(username, password);

        assertNull(result);
    }
}
