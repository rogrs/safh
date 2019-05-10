import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <MenuItem icon="asterisk" to="/entity/clinicas">
      Clinicas
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/dietas">
      Dietas
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/enfermarias">
      Enfermarias
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/especialidades">
      Especialidades
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/fabricantes">
      Fabricantes
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/internacoes">
      Internacoes
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/internacoes-detalhes">
      Internacoes Detalhes
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/leitos">
      Leitos
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/medicamentos">
      Medicamentos
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/medicos">
      Medicos
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/pacientes">
      Pacientes
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/posologias">
      Posologias
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/prescricoes">
      Prescricoes
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
