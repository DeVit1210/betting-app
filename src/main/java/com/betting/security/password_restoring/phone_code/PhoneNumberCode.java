package com.betting.security.password_restoring.phone_code;

import com.betting.user.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "phone_codes")
@Getter
@Setter
@NoArgsConstructor
public class PhoneNumberCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    @ManyToOne
    @JoinColumn(nullable = false, name = "player_id")
    private Player player;

    public PhoneNumberCode(String code, LocalDateTime createdAt, LocalDateTime expiresAt, Player player) {
        this.code = code;
        this.phoneNumber = player.getPhoneNumber();
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.player = player;
    }
}
