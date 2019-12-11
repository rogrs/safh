package br.com.rogrs.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rogrs.web.rest.TestUtil;

public class LeitosTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Leitos.class);
        Leitos leitos1 = new Leitos();
        leitos1.setId("id1");
        Leitos leitos2 = new Leitos();
        leitos2.setId(leitos1.getId());
        assertThat(leitos1).isEqualTo(leitos2);
        leitos2.setId("id2");
        assertThat(leitos1).isNotEqualTo(leitos2);
        leitos1.setId(null);
        assertThat(leitos1).isNotEqualTo(leitos2);
    }
}
