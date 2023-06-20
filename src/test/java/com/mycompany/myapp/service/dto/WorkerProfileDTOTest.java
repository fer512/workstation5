package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkerProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkerProfileDTO.class);
        WorkerProfileDTO workerProfileDTO1 = new WorkerProfileDTO();
        workerProfileDTO1.setId(1L);
        WorkerProfileDTO workerProfileDTO2 = new WorkerProfileDTO();
        assertThat(workerProfileDTO1).isNotEqualTo(workerProfileDTO2);
        workerProfileDTO2.setId(workerProfileDTO1.getId());
        assertThat(workerProfileDTO1).isEqualTo(workerProfileDTO2);
        workerProfileDTO2.setId(2L);
        assertThat(workerProfileDTO1).isNotEqualTo(workerProfileDTO2);
        workerProfileDTO1.setId(null);
        assertThat(workerProfileDTO1).isNotEqualTo(workerProfileDTO2);
    }
}
