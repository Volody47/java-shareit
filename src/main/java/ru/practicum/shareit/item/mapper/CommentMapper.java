package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentForItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;


@Component
@Mapper(componentModel = "spring") //Creates a Spring Bean automatically
public interface CommentMapper {

    @Mapping(source = "id", target = "id")
    List<CommentForItemDto> mapToCommentsForItemDto(List<Comment> comments);

    @Mapping(source = "id", target = "id")
    CommentForItemDto mapToCommentForItemDto(Comment comments);
}
