//package ru.practicum.shareit.user;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import ru.practicum.shareit.exceptions.InvalidEmailException;
//import ru.practicum.shareit.user.model.User;
//import ru.practicum.shareit.user.service.UserServiceForDbImpl;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static ru.practicum.shareit.utils.Validator.validateUser;
//
//class UserControllerTest {
//    private MockMvc restMvc;
//    private UserServiceForDbImpl userService;
//
//    @BeforeEach
//    public void setUp() {
//        userService = mock(UserServiceForDbImpl.class);
//        UserController userController = new UserController(userService);
//        this.restMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    void validateEmail() {
//        User user = new User();
//        user.setEmail("");
//        assertThrows(InvalidEmailException.class, () -> validateUser(user));
//    }
//
//    @Test
//    void shouldFindAllUsersTest() throws Exception {
//        User user = new User();
//        user.setEmail("mail@mail.ru");
//        user.setName("Nick");
//
//
//        when(userService.findAllUsers()).thenReturn(List.of(user));
//
//        restMvc.perform(
//                get("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("utf-8")
//                        .accept(MediaType.APPLICATION_JSON)
//        )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].email").value("mail@mail.ru"));
//    }
//
//    @Test
//    void createUserTest() throws Exception {
//        restMvc.perform(
//                post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\": \"Nick Name\", \"email\": \"mail@mail.ru\"}")
//                        .characterEncoding("utf-8")
//        )
//                .andDo(print())
//                .andExpect(status().isOk());
//
//        verify(userService).createUser(any(User.class));
//    }
//
//    @Test
//    void shouldUpdateUserTest() throws Exception {
//        int id = 1;
//        restMvc.perform(
//                patch("/users/{id}", id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\": \"Nick Update\", \"email\": \"mail@mail.ru\"}")
//                        .characterEncoding("utf-8")
//        )
//                .andDo(print())
//                .andExpect(status().isOk());
//
//        verify(userService).updateUser(any(User.class));
//    }
//
//    @Test
//    void shouldGetUserTest() throws Exception {
//        int id = 35;
//        User user = new User();
//        user.setId(id);
//        user.setEmail("mail@mail.ru");
//        user.setName("Nick");
//
//        when(userService.getUser(id)).thenReturn(user);
//
//        restMvc.perform(
//                get("/users/{id}", id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("utf-8")
//                        .accept(MediaType.APPLICATION_JSON)
//        )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(35));
//    }
//
//    @Test
//    void shouldRemoveUserTest() throws Exception {
//        int userId = 1;
//        restMvc.perform(
//                delete("/users/{userId}", userId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("utf-8")
//                        .accept(MediaType.APPLICATION_JSON)
//        )
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//}