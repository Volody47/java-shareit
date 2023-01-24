package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceForDbImpl;

import java.util.List;


@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserServiceForDbImpl userService;

    @Autowired
    public UserController(UserServiceForDbImpl userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> findAll() {
        return userService.findAllUsers();
    }

    @RequestMapping(method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public User updateUser(@RequestBody User user, @PathVariable int id) {
        user.setId(id);
        return userService.updateUser(user);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    public void removeUser(@PathVariable Integer userId) {
        userService.removeUser(userId);
    }

}
