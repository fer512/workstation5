package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaitingRoomAttencionChannelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaitingRoomAttencionChannelDTO.class);
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO1 = new WaitingRoomAttencionChannelDTO();
        waitingRoomAttencionChannelDTO1.setId(1L);
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO2 = new WaitingRoomAttencionChannelDTO();
        assertThat(waitingRoomAttencionChannelDTO1).isNotEqualTo(waitingRoomAttencionChannelDTO2);
        waitingRoomAttencionChannelDTO2.setId(waitingRoomAttencionChannelDTO1.getId());
        assertThat(waitingRoomAttencionChannelDTO1).isEqualTo(waitingRoomAttencionChannelDTO2);
        waitingRoomAttencionChannelDTO2.setId(2L);
        assertThat(waitingRoomAttencionChannelDTO1).isNotEqualTo(waitingRoomAttencionChannelDTO2);
        waitingRoomAttencionChannelDTO1.setId(null);
        assertThat(waitingRoomAttencionChannelDTO1).isNotEqualTo(waitingRoomAttencionChannelDTO2);
    }
}
