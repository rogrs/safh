package br.com.rogrs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.rogrs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrescricoesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prescricoes.class);
        Prescricoes prescricoes1 = new Prescricoes();
        prescricoes1.setId(1L);
        Prescricoes prescricoes2 = new Prescricoes();
        prescricoes2.setId(prescricoes1.getId());
        assertThat(prescricoes1).isEqualTo(prescricoes2);
        prescricoes2.setId(2L);
        assertThat(prescricoes1).isNotEqualTo(prescricoes2);
        prescricoes1.setId(null);
        assertThat(prescricoes1).isNotEqualTo(prescricoes2);
    }
}
