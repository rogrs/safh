package br.com.rogrs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.rogrs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DietasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dietas.class);
        Dietas dietas1 = new Dietas();
        dietas1.setId(1L);
        Dietas dietas2 = new Dietas();
        dietas2.setId(dietas1.getId());
        assertThat(dietas1).isEqualTo(dietas2);
        dietas2.setId(2L);
        assertThat(dietas1).isNotEqualTo(dietas2);
        dietas1.setId(null);
        assertThat(dietas1).isNotEqualTo(dietas2);
    }
}
