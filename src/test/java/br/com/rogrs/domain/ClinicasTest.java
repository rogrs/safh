package br.com.rogrs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.rogrs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClinicasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Clinicas.class);
        Clinicas clinicas1 = new Clinicas();
        clinicas1.setId(1L);
        Clinicas clinicas2 = new Clinicas();
        clinicas2.setId(clinicas1.getId());
        assertThat(clinicas1).isEqualTo(clinicas2);
        clinicas2.setId(2L);
        assertThat(clinicas1).isNotEqualTo(clinicas2);
        clinicas1.setId(null);
        assertThat(clinicas1).isNotEqualTo(clinicas2);
    }
}
