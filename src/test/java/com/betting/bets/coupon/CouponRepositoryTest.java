package com.betting.bets.coupon;

import com.betting.test_builder.impl.CouponBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CouponRepositoryTest {
    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        CouponBuilder couponBuilder = CouponBuilder.aCouponBuilder();
        couponRepository.saveAll(List.of(
                couponBuilder.withCouponState(CouponState.CONFIRMED).build(),
                couponBuilder.withCouponState(CouponState.EXECUTED).build(),
                couponBuilder.withCouponState(CouponState.EXECUTED).build(),
                couponBuilder.withCouponState(CouponState.CONFIRMED).build(),
                couponBuilder.withCouponState(CouponState.CLOSED).build(),
                couponBuilder.withCouponState(CouponState.EXECUTED).build(),
                couponBuilder.withCouponState(CouponState.EXECUTED).build()
        ));
    }

    @AfterEach
    void tearDown() {
        couponRepository.deleteAll();
    }

    static Stream<Arguments> successArgumentsProvider() {
        return Stream.of(
                Arguments.of(CouponState.CONFIRMED, 2),
                Arguments.of(CouponState.CLOSED, 1),
                Arguments.of(CouponState.EXECUTED, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("successArgumentsProvider")
    void testFindAllByStateSuccess(CouponState couponState, int expectedQuantity) {
        Streamable<Coupon> result = couponRepository.findAllByState(couponState);
        assertEquals(expectedQuantity, result.toList().size());
    }
}