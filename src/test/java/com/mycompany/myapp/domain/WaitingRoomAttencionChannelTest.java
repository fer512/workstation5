package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaitingRoomAttencionChannelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaitingRoomAttencionChannel.class);
        WaitingRoomAttencionChannel waitingRoomAttencionChannel1 = new WaitingRoomAttencionChannel();
        waitingRoomAttencionChannel1.setId(1L);
        WaitingRoomAttencionChannel waitingRoomAttencionChannel2 = new WaitingRoomAttencionChannel();
        waitingRoomAttencionChannel2.setId(waitingRoomAttencionChannel1.getId());
        assertThat(waitingRoomAttencionChannel1).isEqualTo(waitingRoomAttencionChannel2);
        waitingRoomAttencionChannel2.setId(2L);
        assertThat(waitingRoomAttencionChannel1).isNotEqualTo(waitingRoomAttencionChannel2);
        waitingRoomAttencionChannel1.setId(null);
        assertThat(waitingRoomAttencionChannel1).isNotEqualTo(waitingRoomAttencionChannel2);
    }
}
