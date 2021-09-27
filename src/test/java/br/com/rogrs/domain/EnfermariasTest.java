package br.com.rogrs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.rogrs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnfermariasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enfermarias.class);
        Enfermarias enfermarias1 = new Enfermarias();
        enfermarias1.setId(1L);
        Enfermarias enfermarias2 = new Enfermarias();
        enfermarias2.setId(enfermarias1.getId());
        assertThat(enfermarias1).isEqualTo(enfermarias2);
        enfermarias2.setId(2L);
        assertThat(enfermarias1).isNotEqualTo(enfermarias2);
        enfermarias1.setId(null);
        assertThat(enfermarias1).isNotEqualTo(enfermarias2);
    }
}
