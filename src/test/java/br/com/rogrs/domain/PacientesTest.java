package br.com.rogrs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.rogrs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PacientesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pacientes.class);
        Pacientes pacientes1 = new Pacientes();
        pacientes1.setId(1L);
        Pacientes pacientes2 = new Pacientes();
        pacientes2.setId(pacientes1.getId());
        assertThat(pacientes1).isEqualTo(pacientes2);
        pacientes2.setId(2L);
        assertThat(pacientes1).isNotEqualTo(pacientes2);
        pacientes1.setId(null);
        assertThat(pacientes1).isNotEqualTo(pacientes2);
    }
}
