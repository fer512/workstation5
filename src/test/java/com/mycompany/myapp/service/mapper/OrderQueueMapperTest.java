package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderQueueMapperTest {

    private OrderQueueMapper orderQueueMapper;

    @BeforeEach
    public void setUp() {
        orderQueueMapper = new OrderQueueMapperImpl();
    }
}
