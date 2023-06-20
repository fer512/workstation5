package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaitingRoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaitingRoom.class);
        WaitingRoom waitingRoom1 = new WaitingRoom();
        waitingRoom1.setId(1L);
        WaitingRoom waitingRoom2 = new WaitingRoom();
        waitingRoom2.setId(waitingRoom1.getId());
        assertThat(waitingRoom1).isEqualTo(waitingRoom2);
        waitingRoom2.setId(2L);
        assertThat(waitingRoom1).isNotEqualTo(waitingRoom2);
        waitingRoom1.setId(null);
        assertThat(waitingRoom1).isNotEqualTo(waitingRoom2);
    }
}
