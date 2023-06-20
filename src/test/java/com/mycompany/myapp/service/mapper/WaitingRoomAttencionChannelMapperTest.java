package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaitingRoomAttencionChannelMapperTest {

    private WaitingRoomAttencionChannelMapper waitingRoomAttencionChannelMapper;

    @BeforeEach
    public void setUp() {
        waitingRoomAttencionChannelMapper = new WaitingRoomAttencionChannelMapperImpl();
    }
}
