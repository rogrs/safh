package br.com.rogrs.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rogrs.web.rest.TestUtil;

public class EspecialidadesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Especialidades.class);
        Especialidades especialidades1 = new Especialidades();
        especialidades1.setId("id1");
        Especialidades especialidades2 = new Especialidades();
        especialidades2.setId(especialidades1.getId());
        assertThat(especialidades1).isEqualTo(especialidades2);
        especialidades2.setId("id2");
        assertThat(especialidades1).isNotEqualTo(especialidades2);
        especialidades1.setId(null);
        assertThat(especialidades1).isNotEqualTo(especialidades2);
    }
}
