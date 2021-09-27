package br.com.rogrs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.rogrs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FabricantesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fabricantes.class);
        Fabricantes fabricantes1 = new Fabricantes();
        fabricantes1.setId(1L);
        Fabricantes fabricantes2 = new Fabricantes();
        fabricantes2.setId(fabricantes1.getId());
        assertThat(fabricantes1).isEqualTo(fabricantes2);
        fabricantes2.setId(2L);
        assertThat(fabricantes1).isNotEqualTo(fabricantes2);
        fabricantes1.setId(null);
        assertThat(fabricantes1).isNotEqualTo(fabricantes2);
    }
}
