package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderQueueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderQueue.class);
        OrderQueue orderQueue1 = new OrderQueue();
        orderQueue1.setId(1L);
        OrderQueue orderQueue2 = new OrderQueue();
        orderQueue2.setId(orderQueue1.getId());
        assertThat(orderQueue1).isEqualTo(orderQueue2);
        orderQueue2.setId(2L);
        assertThat(orderQueue1).isNotEqualTo(orderQueue2);
        orderQueue1.setId(null);
        assertThat(orderQueue1).isNotEqualTo(orderQueue2);
    }
}
