package com.betting.bets.stake_type;

import com.betting.events.sport.SportService;
import com.betting.security.auth.mapping.StakeTypeRequestMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class StakeTypeServiceTest {
    @InjectMocks
    private StakeTypeService stakeTypeService;
    @Mock
    private StakeTypeRepository stakeTypeRepository;
    @Mock
    private SportService sportService;
    @Mock
    private BeanFactory beanFactory;
    @Mock
    private StakeTypeRequestMapper mapper;
    @Mock
    private StakeTypeFactory stakeTypeFactory;
    @Test
    void testAddStakeTypeSuccess() {
        when(beanFactory.getBean(StakeTypeRequestMapper.class, sportService, stakeTypeFactory)).thenReturn(mapper);
        StakeType stakeType = mock(StakeType.class);
        StakeTypeRequest request = mock(StakeTypeRequest.class);
        when(mapper.mapFrom(request)).thenReturn(stakeType);
        when(stakeTypeRepository.save(stakeType)).thenReturn(stakeType);
        String result = stakeTypeService.addStakeType(request);
        assertEquals("stake type successfully added!", result);
    }
}