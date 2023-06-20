package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkerProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkerProfile.class);
        WorkerProfile workerProfile1 = new WorkerProfile();
        workerProfile1.setId(1L);
        WorkerProfile workerProfile2 = new WorkerProfile();
        workerProfile2.setId(workerProfile1.getId());
        assertThat(workerProfile1).isEqualTo(workerProfile2);
        workerProfile2.setId(2L);
        assertThat(workerProfile1).isNotEqualTo(workerProfile2);
        workerProfile1.setId(null);
        assertThat(workerProfile1).isNotEqualTo(workerProfile2);
    }
}
