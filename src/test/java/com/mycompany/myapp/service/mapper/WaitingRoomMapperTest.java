package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaitingRoomMapperTest {

    private WaitingRoomMapper waitingRoomMapper;

    @BeforeEach
    public void setUp() {
        waitingRoomMapper = new WaitingRoomMapperImpl();
    }
}
