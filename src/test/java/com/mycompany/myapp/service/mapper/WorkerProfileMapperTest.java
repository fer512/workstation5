package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkerProfileMapperTest {

    private WorkerProfileMapper workerProfileMapper;

    @BeforeEach
    public void setUp() {
        workerProfileMapper = new WorkerProfileMapperImpl();
    }
}
