package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentForItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceForDbImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceForDbImpl;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceForDbImpl itemService;
    private final UserServiceForDbImpl userService;


    @Autowired
    public ItemController(ItemServiceForDbImpl itemService, UserServiceForDbImpl userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping
    @RequestMapping(method = RequestMethod.GET)
    public List<ItemDto> findAll(@RequestHeader(value = "X-Sharer-User-Id") int ownerId) {
        User user = userService.getUser(ownerId);
        return itemService.findAllItems(user);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ItemDto createItem(@RequestBody Item item,
                              @RequestHeader(value = "X-Sharer-User-Id") int ownerId) {
        User user = userService.getUser(ownerId);
        return itemService.createItem(item, user);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{itemId}")
    public ItemDto updateItem(@RequestBody Item item,
                           @RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                           @PathVariable int itemId) {
        User user = userService.getUser(ownerId);
        item.setId(itemId);
        return itemService.updateItem(item, user);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{itemId}")
    public ItemDto getItem(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                        @PathVariable int itemId) {
        User user = userService.getUser(ownerId);
        return itemService.getItemDto(itemId, user);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public List<ItemDto> findItemsBaseOnRequest(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                        @RequestParam(value = "text", required = false) String text) {
        User user = userService.getUser(ownerId);
        return itemService.findItemsBaseOnRequest(text);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{itemId}")
    public void removeItem(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                           @PathVariable int itemId) {
        itemService.removeItem(itemId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{itemId}/comment")
    public CommentForItemDto createComment(@RequestBody Comment comment,
                                           @RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                                           @PathVariable int itemId) {
        User user = userService.getUser(ownerId);
        ItemDto itemDto = itemService.getItemDto(itemId, user);
        return itemService.createComment(comment, user, itemDto);
    }
}
