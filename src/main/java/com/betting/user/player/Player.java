package com.betting.user.player;

import com.betting.security.config.ApplicationUserRole;
import com.betting.user.AppUser;
import com.betting.user.Bet;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Player extends AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String passwordSeries;
    private String passwordNumber;
    private String phoneNumber;
    @OneToMany(mappedBy = "player")
    private List<Bet> bets;

    // TODO: @OneToMany List<Bets>

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(ApplicationUserRole.PLAYER.getPermissions());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + ApplicationUserRole.PLAYER.name()));
        return authorities;
    }
}
