package com.platform.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrackerMapperTest {

    private TrackerMapper trackerMapper;

    @BeforeEach
    public void setUp() {
        trackerMapper = new TrackerMapperImpl();
    }
}
