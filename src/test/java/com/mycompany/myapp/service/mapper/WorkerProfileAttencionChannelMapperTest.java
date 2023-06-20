package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkerProfileAttencionChannelMapperTest {

    private WorkerProfileAttencionChannelMapper workerProfileAttencionChannelMapper;

    @BeforeEach
    public void setUp() {
        workerProfileAttencionChannelMapper = new WorkerProfileAttencionChannelMapperImpl();
    }
}
