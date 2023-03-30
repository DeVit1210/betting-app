package com.betting.security.password_restoring.phone_code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PhoneNumberCodeRepository extends JpaRepository<PhoneNumberCode, Long> {
    Optional<PhoneNumberCode> findByCode(String code);

    @Transactional
    @Modifying
    @Query("update PhoneNumberCode c set c.confirmedAt=?2 where c.code=?1")
    void confirm(String code, LocalDateTime time);
}
