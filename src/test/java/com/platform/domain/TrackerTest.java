package com.platform.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.platform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrackerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tracker.class);
        Tracker tracker1 = new Tracker();
        tracker1.setId("id1");
        Tracker tracker2 = new Tracker();
        tracker2.setId(tracker1.getId());
        assertThat(tracker1).isEqualTo(tracker2);
        tracker2.setId("id2");
        assertThat(tracker1).isNotEqualTo(tracker2);
        tracker1.setId(null);
        assertThat(tracker1).isNotEqualTo(tracker2);
    }
}
