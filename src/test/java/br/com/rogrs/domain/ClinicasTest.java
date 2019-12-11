package br.com.rogrs.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rogrs.web.rest.TestUtil;

public class ClinicasTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Clinicas.class);
        Clinicas clinicas1 = new Clinicas();
        clinicas1.setId("id1");
        Clinicas clinicas2 = new Clinicas();
        clinicas2.setId(clinicas1.getId());
        assertThat(clinicas1).isEqualTo(clinicas2);
        clinicas2.setId("id2");
        assertThat(clinicas1).isNotEqualTo(clinicas2);
        clinicas1.setId(null);
        assertThat(clinicas1).isNotEqualTo(clinicas2);
    }
}
