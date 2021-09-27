package br.com.rogrs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.rogrs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EspecialidadesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Especialidades.class);
        Especialidades especialidades1 = new Especialidades();
        especialidades1.setId(1L);
        Especialidades especialidades2 = new Especialidades();
        especialidades2.setId(especialidades1.getId());
        assertThat(especialidades1).isEqualTo(especialidades2);
        especialidades2.setId(2L);
        assertThat(especialidades1).isNotEqualTo(especialidades2);
        especialidades1.setId(null);
        assertThat(especialidades1).isNotEqualTo(especialidades2);
    }
}
