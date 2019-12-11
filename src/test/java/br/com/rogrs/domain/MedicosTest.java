package br.com.rogrs.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rogrs.web.rest.TestUtil;

public class MedicosTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicos.class);
        Medicos medicos1 = new Medicos();
        medicos1.setId("id1");
        Medicos medicos2 = new Medicos();
        medicos2.setId(medicos1.getId());
        assertThat(medicos1).isEqualTo(medicos2);
        medicos2.setId("id2");
        assertThat(medicos1).isNotEqualTo(medicos2);
        medicos1.setId(null);
        assertThat(medicos1).isNotEqualTo(medicos2);
    }
}
