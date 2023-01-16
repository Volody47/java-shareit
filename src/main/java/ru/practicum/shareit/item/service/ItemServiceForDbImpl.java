package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.CommentCreationException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.OwnerNotFoundForItemException;
import ru.practicum.shareit.item.dto.CommentForItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemDbStorageImpl;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceForDbImpl implements ItemService {
    private final ItemDbStorageImpl itemStorage;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;


    @Autowired
    public ItemServiceForDbImpl(ItemDbStorageImpl itemStorage, BookingRepository bookingRepository, CommentRepository commentRepository, ItemMapper itemMapper, BookingMapper bookingMapper, CommentMapper commentMapper) {
        this.itemStorage = itemStorage;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemMapper = itemMapper;
        this.bookingMapper = bookingMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public ItemDto createItem(Item item, User user) {
        return itemStorage.createItem(item, user);
    }

    @Override
    public ItemDto updateItem(Item item, User user) {
        if (itemStorage.findItemOwner(item).getId() != user.getId()) {
            throw new OwnerNotFoundForItemException("This item not belongs to User with id=" + user.getId());
        }
        ItemDto updatedItem = itemStorage.updateItem(item, user);
        if (updatedItem == null) {
            throw new ItemNotFoundException("Item with id=" + item.getId() + " not found.");
        }
        return updatedItem;
    }

    @Override
    public ItemDto getItemDto(int id, User user) {
        ItemDto item = itemStorage.getItemDto(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with id=" + id + " not found.");
        }
        List<Comment> commentsList = commentRepository.getCommentsByItem(itemMapper.mapToItem(item));
        item.setComments(commentMapper.mapToCommentsForItemDto(commentsList));
        if (!user.equals(item.getOwner())) {
            return item;
        } else {
            List<Booking> bookingList = bookingRepository.getBookingsByItem(itemMapper.mapToItem(item));
            List<Booking> bookingInPast = bookingList.stream()
                    .filter(booking -> (booking.getStatus().equals("APPROVED")))
                    .sorted(Comparator.comparing(Booking::getEnd, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
            List<Booking> bookingInFuture = bookingList.stream()
                    .filter(booking -> (booking.getStatus().equals("APPROVED")))
                    .filter(booking -> (booking.getStart().isAfter(LocalDateTime.now())))
                    .sorted(Comparator.comparing(Booking::getStart, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
            if (bookingInPast.size() != 0) {
                item.setLastBooking(bookingMapper.mapToBookingForItemDto(bookingInPast.get(0)));
            }
            if (bookingInFuture.size() != 0) {
                item.setNextBooking(bookingMapper.mapToBookingForItemDto(bookingInFuture.get(0)));
            }
            return item;
        }
    }

    @Override
    public Item getItem(int id, User user) {
        Item item = itemStorage.getItem(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with id=" + id + " not found.");
        }
        return item;
    }

    @Override
    public void removeItem(Integer itemId) {
        ItemDto item = itemStorage.getItemDto(itemId);
        if (item == null) {
            throw new ItemNotFoundException("Item with id=" + itemId + " not found.");
        }
        itemStorage.removeItem(itemId);

    }

    @Override
    public List<ItemDto> findAllItems(User user) {
        log.debug("Items quantity: {}", itemStorage.findAllItems(user).size());
        List<ItemDto> allItems = itemStorage.findAllItems(user);
        for (ItemDto item : allItems) {
            List<Booking> bookingList = bookingRepository.getBookingsByItem(itemMapper.mapToItem(item));
            List<Booking> bookingInPast = bookingList.stream()
                    .filter(booking -> (booking.getStatus().equals("APPROVED")))
                    .sorted(Comparator.comparing(Booking::getEnd, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
            List<Booking> bookingInFuture = bookingList.stream()
                    .filter(booking -> (booking.getStatus().equals("APPROVED")))
                    .filter(booking -> (booking.getStart().isAfter(LocalDateTime.now())))
                    .sorted(Comparator.comparing(Booking::getStart, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
            if (bookingInPast.size() != 0) {
                item.setLastBooking(bookingMapper.mapToBookingForItemDto(bookingInPast.get(0)));
            }
            if (bookingInFuture.size() != 0) {
                item.setNextBooking(bookingMapper.mapToBookingForItemDto(bookingInFuture.get(0)));
            }
        }
        return allItems;

    }

    @Override
    public List<ItemDto> findItemsBaseOnRequest(String text) {
        if (text == null || text.isBlank()) {
            log.error("Text can't be empty");
            return List.of();
        }
        String textInLowerCase = text.toLowerCase();

        return itemStorage.findItemsBaseOnRequest(textInLowerCase);
    }

    @Override
    public CommentForItemDto createComment(Comment comment, User user, ItemDto itemDto) {
        List<Booking> bookingList = bookingRepository.getBookingsByItem(itemMapper.mapToItem(itemDto));
        List<Booking> userCanCreateComment = bookingList.stream()
                .filter(booking -> (booking.getBooker().equals(user)))
                .filter(booking -> (booking.getStatus().equals("APPROVED")))
                .filter(booking -> (booking.getStart().isBefore(LocalDateTime.now())
                        && booking.getEnd().isBefore(LocalDateTime.now())))
                .collect(Collectors.toList());
        if (userCanCreateComment.size() == 0) {
            throw new CommentCreationException("User with id=" + user.getId() + " can't create comment.");
        } else if (comment.getText().equals("")) {
            throw new CommentCreationException("User can't create empty comment.");
        } else {
            comment.setAuthor(user);
            comment.setItem(itemMapper.mapToItem(itemDto));
            comment.setCreated(LocalDateTime.now());
            commentRepository.save(comment);
            return commentMapper.mapToCommentForItemDto(comment);

        }
    }
}
