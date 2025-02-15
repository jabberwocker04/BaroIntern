package BaroIntern.onboarding.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authorities role;

    private User(
            String username,
            String password,
            String nickname,
            Authorities role
    ) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public static User create(
            String username,
            String password,
            String nickname,
            Authorities role) {
        return new User(
                username,
                password,
                nickname,
                role
        );
    }
}
