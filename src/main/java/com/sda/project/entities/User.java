package com.sda.project.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    //@NotBlank
    @Enumerated(value = EnumType.STRING)
    private UserGender gender;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private String profilePicture;

    @Enumerated(value = EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "firstUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Friend> friends;

}