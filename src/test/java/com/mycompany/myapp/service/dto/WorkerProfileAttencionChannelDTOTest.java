package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkerProfileAttencionChannelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkerProfileAttencionChannelDTO.class);
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO1 = new WorkerProfileAttencionChannelDTO();
        workerProfileAttencionChannelDTO1.setId(1L);
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO2 = new WorkerProfileAttencionChannelDTO();
        assertThat(workerProfileAttencionChannelDTO1).isNotEqualTo(workerProfileAttencionChannelDTO2);
        workerProfileAttencionChannelDTO2.setId(workerProfileAttencionChannelDTO1.getId());
        assertThat(workerProfileAttencionChannelDTO1).isEqualTo(workerProfileAttencionChannelDTO2);
        workerProfileAttencionChannelDTO2.setId(2L);
        assertThat(workerProfileAttencionChannelDTO1).isNotEqualTo(workerProfileAttencionChannelDTO2);
        workerProfileAttencionChannelDTO1.setId(null);
        assertThat(workerProfileAttencionChannelDTO1).isNotEqualTo(workerProfileAttencionChannelDTO2);
    }
}
