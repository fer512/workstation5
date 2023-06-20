package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkerProfileAttencionChannelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkerProfileAttencionChannel.class);
        WorkerProfileAttencionChannel workerProfileAttencionChannel1 = new WorkerProfileAttencionChannel();
        workerProfileAttencionChannel1.setId(1L);
        WorkerProfileAttencionChannel workerProfileAttencionChannel2 = new WorkerProfileAttencionChannel();
        workerProfileAttencionChannel2.setId(workerProfileAttencionChannel1.getId());
        assertThat(workerProfileAttencionChannel1).isEqualTo(workerProfileAttencionChannel2);
        workerProfileAttencionChannel2.setId(2L);
        assertThat(workerProfileAttencionChannel1).isNotEqualTo(workerProfileAttencionChannel2);
        workerProfileAttencionChannel1.setId(null);
        assertThat(workerProfileAttencionChannel1).isNotEqualTo(workerProfileAttencionChannel2);
    }
}
