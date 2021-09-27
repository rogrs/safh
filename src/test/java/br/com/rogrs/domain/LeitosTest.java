package br.com.rogrs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.rogrs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeitosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Leitos.class);
        Leitos leitos1 = new Leitos();
        leitos1.setId(1L);
        Leitos leitos2 = new Leitos();
        leitos2.setId(leitos1.getId());
        assertThat(leitos1).isEqualTo(leitos2);
        leitos2.setId(2L);
        assertThat(leitos1).isNotEqualTo(leitos2);
        leitos1.setId(null);
        assertThat(leitos1).isNotEqualTo(leitos2);
    }
}
