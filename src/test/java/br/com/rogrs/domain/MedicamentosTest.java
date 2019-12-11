package br.com.rogrs.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rogrs.web.rest.TestUtil;

public class MedicamentosTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicamentos.class);
        Medicamentos medicamentos1 = new Medicamentos();
        medicamentos1.setId("id1");
        Medicamentos medicamentos2 = new Medicamentos();
        medicamentos2.setId(medicamentos1.getId());
        assertThat(medicamentos1).isEqualTo(medicamentos2);
        medicamentos2.setId("id2");
        assertThat(medicamentos1).isNotEqualTo(medicamentos2);
        medicamentos1.setId(null);
        assertThat(medicamentos1).isNotEqualTo(medicamentos2);
    }
}
