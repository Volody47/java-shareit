package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "USERS")
public class User {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "USER_FULL_NAME", nullable = false, length = 300)
    private String name;

    @Column(name = "USER_EMAIL", nullable = false, length = 300)
    private String email;
}
