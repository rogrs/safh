package br.com.rogrs.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rogrs.web.rest.TestUtil;

public class PosologiasTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Posologias.class);
        Posologias posologias1 = new Posologias();
        posologias1.setId("id1");
        Posologias posologias2 = new Posologias();
        posologias2.setId(posologias1.getId());
        assertThat(posologias1).isEqualTo(posologias2);
        posologias2.setId("id2");
        assertThat(posologias1).isNotEqualTo(posologias2);
        posologias1.setId(null);
        assertThat(posologias1).isNotEqualTo(posologias2);
    }
}
