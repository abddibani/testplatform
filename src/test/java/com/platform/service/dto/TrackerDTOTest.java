package com.platform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.platform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrackerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrackerDTO.class);
        TrackerDTO trackerDTO1 = new TrackerDTO();
        trackerDTO1.setId("id1");
        TrackerDTO trackerDTO2 = new TrackerDTO();
        assertThat(trackerDTO1).isNotEqualTo(trackerDTO2);
        trackerDTO2.setId(trackerDTO1.getId());
        assertThat(trackerDTO1).isEqualTo(trackerDTO2);
        trackerDTO2.setId("id2");
        assertThat(trackerDTO1).isNotEqualTo(trackerDTO2);
        trackerDTO1.setId(null);
        assertThat(trackerDTO1).isNotEqualTo(trackerDTO2);
    }
}
