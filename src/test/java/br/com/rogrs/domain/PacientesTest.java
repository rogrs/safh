package br.com.rogrs.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rogrs.web.rest.TestUtil;

public class PacientesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pacientes.class);
        Pacientes pacientes1 = new Pacientes();
        pacientes1.setId("id1");
        Pacientes pacientes2 = new Pacientes();
        pacientes2.setId(pacientes1.getId());
        assertThat(pacientes1).isEqualTo(pacientes2);
        pacientes2.setId("id2");
        assertThat(pacientes1).isNotEqualTo(pacientes2);
        pacientes1.setId(null);
        assertThat(pacientes1).isNotEqualTo(pacientes2);
    }
}
