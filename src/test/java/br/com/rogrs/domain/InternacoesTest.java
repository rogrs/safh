package br.com.rogrs.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rogrs.web.rest.TestUtil;

public class InternacoesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Internacoes.class);
        Internacoes internacoes1 = new Internacoes();
        internacoes1.setId("id1");
        Internacoes internacoes2 = new Internacoes();
        internacoes2.setId(internacoes1.getId());
        assertThat(internacoes1).isEqualTo(internacoes2);
        internacoes2.setId("id2");
        assertThat(internacoes1).isNotEqualTo(internacoes2);
        internacoes1.setId(null);
        assertThat(internacoes1).isNotEqualTo(internacoes2);
    }
}
