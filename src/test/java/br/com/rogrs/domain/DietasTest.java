package br.com.rogrs.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rogrs.web.rest.TestUtil;

public class DietasTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dietas.class);
        Dietas dietas1 = new Dietas();
        dietas1.setId("id1");
        Dietas dietas2 = new Dietas();
        dietas2.setId(dietas1.getId());
        assertThat(dietas1).isEqualTo(dietas2);
        dietas2.setId("id2");
        assertThat(dietas1).isNotEqualTo(dietas2);
        dietas1.setId(null);
        assertThat(dietas1).isNotEqualTo(dietas2);
    }
}
