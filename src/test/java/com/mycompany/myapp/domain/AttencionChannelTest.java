package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttencionChannelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttencionChannel.class);
        AttencionChannel attencionChannel1 = new AttencionChannel();
        attencionChannel1.setId(1L);
        AttencionChannel attencionChannel2 = new AttencionChannel();
        attencionChannel2.setId(attencionChannel1.getId());
        assertThat(attencionChannel1).isEqualTo(attencionChannel2);
        attencionChannel2.setId(2L);
        assertThat(attencionChannel1).isNotEqualTo(attencionChannel2);
        attencionChannel1.setId(null);
        assertThat(attencionChannel1).isNotEqualTo(attencionChannel2);
    }
}
