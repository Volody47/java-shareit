package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;


    @Autowired
    public ItemController(ItemServiceImpl itemService, UserServiceImpl userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping
    public List<Item> findAll(@RequestHeader(value = "X-Sharer-User-Id") int ownerId) {
        User user = userService.getUser(ownerId);
        return itemService.findAllItems(user);
    }

    @PostMapping
    public Item createItem(@RequestBody Item item,
                           @RequestHeader(value = "X-Sharer-User-Id") int ownerId) {
        User user = userService.getUser(ownerId);
        return itemService.createItem(item, user);
    }

    @PatchMapping(value = "/{itemId}")
    public Item updateItem(@RequestBody Item item,
                           @RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                           @PathVariable int itemId) {
        User user = userService.getUser(ownerId);
        item.setId(itemId);
        return itemService.updateItem(item, user);
    }

    @GetMapping(value = "/{itemId}")
    public Item getItem(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                        @PathVariable int itemId) {
        User user = userService.getUser(ownerId);
        return itemService.getItem(itemId, user);
    }

    @GetMapping(value = "/search")
    public List<Item> findItemsBaseOnRequest(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                        @RequestParam(value = "text", required = false) String text) {
        User user = userService.getUser(ownerId);
        return itemService.findItemsBaseOnRequest(text);
    }

    @DeleteMapping(value = "/{itemId}")
    public void removeItem(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                           @PathVariable int itemId) {
        itemService.removeItem(itemId);
    }


}
