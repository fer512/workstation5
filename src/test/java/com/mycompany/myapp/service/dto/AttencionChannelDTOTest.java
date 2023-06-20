package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttencionChannelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttencionChannelDTO.class);
        AttencionChannelDTO attencionChannelDTO1 = new AttencionChannelDTO();
        attencionChannelDTO1.setId(1L);
        AttencionChannelDTO attencionChannelDTO2 = new AttencionChannelDTO();
        assertThat(attencionChannelDTO1).isNotEqualTo(attencionChannelDTO2);
        attencionChannelDTO2.setId(attencionChannelDTO1.getId());
        assertThat(attencionChannelDTO1).isEqualTo(attencionChannelDTO2);
        attencionChannelDTO2.setId(2L);
        assertThat(attencionChannelDTO1).isNotEqualTo(attencionChannelDTO2);
        attencionChannelDTO1.setId(null);
        assertThat(attencionChannelDTO1).isNotEqualTo(attencionChannelDTO2);
    }
}
