package com.platform.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.platform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExchangeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExchangeDTO.class);
        ExchangeDTO exchangeDTO1 = new ExchangeDTO();
        exchangeDTO1.setId("id1");
        ExchangeDTO exchangeDTO2 = new ExchangeDTO();
        assertThat(exchangeDTO1).isNotEqualTo(exchangeDTO2);
        exchangeDTO2.setId(exchangeDTO1.getId());
        assertThat(exchangeDTO1).isEqualTo(exchangeDTO2);
        exchangeDTO2.setId("id2");
        assertThat(exchangeDTO1).isNotEqualTo(exchangeDTO2);
        exchangeDTO1.setId(null);
        assertThat(exchangeDTO1).isNotEqualTo(exchangeDTO2);
    }
}
