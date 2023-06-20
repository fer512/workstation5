package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaitingRoomDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaitingRoomDTO.class);
        WaitingRoomDTO waitingRoomDTO1 = new WaitingRoomDTO();
        waitingRoomDTO1.setId(1L);
        WaitingRoomDTO waitingRoomDTO2 = new WaitingRoomDTO();
        assertThat(waitingRoomDTO1).isNotEqualTo(waitingRoomDTO2);
        waitingRoomDTO2.setId(waitingRoomDTO1.getId());
        assertThat(waitingRoomDTO1).isEqualTo(waitingRoomDTO2);
        waitingRoomDTO2.setId(2L);
        assertThat(waitingRoomDTO1).isNotEqualTo(waitingRoomDTO2);
        waitingRoomDTO1.setId(null);
        assertThat(waitingRoomDTO1).isNotEqualTo(waitingRoomDTO2);
    }
}
