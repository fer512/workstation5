package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderQueueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderQueueDTO.class);
        OrderQueueDTO orderQueueDTO1 = new OrderQueueDTO();
        orderQueueDTO1.setId(1L);
        OrderQueueDTO orderQueueDTO2 = new OrderQueueDTO();
        assertThat(orderQueueDTO1).isNotEqualTo(orderQueueDTO2);
        orderQueueDTO2.setId(orderQueueDTO1.getId());
        assertThat(orderQueueDTO1).isEqualTo(orderQueueDTO2);
        orderQueueDTO2.setId(2L);
        assertThat(orderQueueDTO1).isNotEqualTo(orderQueueDTO2);
        orderQueueDTO1.setId(null);
        assertThat(orderQueueDTO1).isNotEqualTo(orderQueueDTO2);
    }
}
