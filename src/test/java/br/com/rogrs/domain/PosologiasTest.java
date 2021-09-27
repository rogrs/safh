package br.com.rogrs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.rogrs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PosologiasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Posologias.class);
        Posologias posologias1 = new Posologias();
        posologias1.setId(1L);
        Posologias posologias2 = new Posologias();
        posologias2.setId(posologias1.getId());
        assertThat(posologias1).isEqualTo(posologias2);
        posologias2.setId(2L);
        assertThat(posologias1).isNotEqualTo(posologias2);
        posologias1.setId(null);
        assertThat(posologias1).isNotEqualTo(posologias2);
    }
}
