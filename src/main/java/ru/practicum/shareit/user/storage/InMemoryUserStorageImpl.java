package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
public class InMemoryUserStorageImpl implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();
    private Integer identificator = 0;

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User getUser(int id) {
        return null;
    }

    @Override
    public void removeUser(User user) {

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
