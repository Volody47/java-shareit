package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

@Component
public interface ItemRepository extends JpaRepository<Item, Integer> {
}
