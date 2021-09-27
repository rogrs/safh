package br.com.rogrs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.rogrs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InternacoesDetalhesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternacoesDetalhes.class);
        InternacoesDetalhes internacoesDetalhes1 = new InternacoesDetalhes();
        internacoesDetalhes1.setId(1L);
        InternacoesDetalhes internacoesDetalhes2 = new InternacoesDetalhes();
        internacoesDetalhes2.setId(internacoesDetalhes1.getId());
        assertThat(internacoesDetalhes1).isEqualTo(internacoesDetalhes2);
        internacoesDetalhes2.setId(2L);
        assertThat(internacoesDetalhes1).isNotEqualTo(internacoesDetalhes2);
        internacoesDetalhes1.setId(null);
        assertThat(internacoesDetalhes1).isNotEqualTo(internacoesDetalhes2);
    }
}
