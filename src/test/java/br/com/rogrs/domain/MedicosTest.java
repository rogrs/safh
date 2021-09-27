package br.com.rogrs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.rogrs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicos.class);
        Medicos medicos1 = new Medicos();
        medicos1.setId(1L);
        Medicos medicos2 = new Medicos();
        medicos2.setId(medicos1.getId());
        assertThat(medicos1).isEqualTo(medicos2);
        medicos2.setId(2L);
        assertThat(medicos1).isNotEqualTo(medicos2);
        medicos1.setId(null);
        assertThat(medicos1).isNotEqualTo(medicos2);
    }
}
