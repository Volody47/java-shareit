package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.practicum.shareit.utils.Validator.validateUser;

@Repository
@Slf4j
public class InMemoryUserStorageImpl implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();
    private Integer identificator = 0;


    public int generateId() {
        return ++identificator;
    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.debug("New user created with id={}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = null;
        for (Integer userId : users.keySet()) {
            if (userId.equals(user.getId())) {
                if (user.getName() == null) {
                    user.setName(users.get(userId).getName());
                } else if (user.getEmail() == null) {
                    user.setEmail(users.get(userId).getEmail());
                }
                validateUser(user);
                users.put(user.getId(), user);
                updatedUser = users.get(user.getId());
                log.debug("User with id={} updated", user.getId());
            }
        }
        return updatedUser;
    }

    @Override
    public User getUser(int id) {
        User user = null;
        for (Integer userId : users.keySet()) {
            if (userId == id) {
                user = users.get(id);
            }
        }
        return user;
    }

    @Override
    public void removeUser(Integer userId) {
        users.remove(userId);
        log.debug("User with id={} removed", userId);
    }

    @Override
    public List<User> findAllUsers() {
        ArrayList<User> listOfUsers = new ArrayList<>();
        for (Integer user : users.keySet()) {
            listOfUsers.add(users.get(user));
        }
        log.debug("Users quantity: {}", listOfUsers.size());
        return listOfUsers;
    }
}
