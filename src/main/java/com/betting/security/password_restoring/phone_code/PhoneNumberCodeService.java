package com.betting.security.password_restoring.phone_code;

import com.betting.user.player.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PhoneNumberCodeService {
    private final PhoneNumberCodeRepository phoneNumberCodeRepository;

    public String generateCode(Player player) {
        Random random = new Random();
        String code = String.valueOf(random.nextInt(100000, 1000000));
        PhoneNumberCode phoneNumberCode = new PhoneNumberCode(
                code, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), player);
        phoneNumberCodeRepository.save(phoneNumberCode);
        return code;
    }

    public PhoneNumberCode findByCode(String code) {
        return phoneNumberCodeRepository.findByCode(code)
                .orElseThrow(IllegalAccessError::new);
    }

    public void verifyNumber(String code) {
        phoneNumberCodeRepository.confirm(code, LocalDateTime.now());
    }
}
