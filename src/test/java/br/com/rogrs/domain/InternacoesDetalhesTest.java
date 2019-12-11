package br.com.rogrs.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rogrs.web.rest.TestUtil;

public class InternacoesDetalhesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternacoesDetalhes.class);
        InternacoesDetalhes internacoesDetalhes1 = new InternacoesDetalhes();
        internacoesDetalhes1.setId("id1");
        InternacoesDetalhes internacoesDetalhes2 = new InternacoesDetalhes();
        internacoesDetalhes2.setId(internacoesDetalhes1.getId());
        assertThat(internacoesDetalhes1).isEqualTo(internacoesDetalhes2);
        internacoesDetalhes2.setId("id2");
        assertThat(internacoesDetalhes1).isNotEqualTo(internacoesDetalhes2);
        internacoesDetalhes1.setId(null);
        assertThat(internacoesDetalhes1).isNotEqualTo(internacoesDetalhes2);
    }
}
