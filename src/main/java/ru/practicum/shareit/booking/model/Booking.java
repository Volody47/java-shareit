package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@Table(name = "bookings")
public class Booking {

    @Id
    @Column(name = "BOOKING_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;

//    @ManyToOne
//    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
//    @Column(name = "item_id", nullable = false)
//    private int itemId;

    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "user_id")
    private User booker;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @Column(name = "booking_status", nullable = false, length = 300)
    private String status;
}
