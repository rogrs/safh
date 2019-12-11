import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name={translate('global.menu.entities.main')} id="entity-menu">
    <MenuItem icon="asterisk" to="/clinicas">
      <Translate contentKey="global.menu.entities.clinicas" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/dietas">
      <Translate contentKey="global.menu.entities.dietas" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/enfermarias">
      <Translate contentKey="global.menu.entities.enfermarias" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/especialidades">
      <Translate contentKey="global.menu.entities.especialidades" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/fabricantes">
      <Translate contentKey="global.menu.entities.fabricantes" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/internacoes">
      <Translate contentKey="global.menu.entities.internacoes" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/internacoes-detalhes">
      <Translate contentKey="global.menu.entities.internacoesDetalhes" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/leitos">
      <Translate contentKey="global.menu.entities.leitos" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/medicamentos">
      <Translate contentKey="global.menu.entities.medicamentos" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/medicos">
      <Translate contentKey="global.menu.entities.medicos" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/pacientes">
      <Translate contentKey="global.menu.entities.pacientes" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/posologias">
      <Translate contentKey="global.menu.entities.posologias" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/prescricoes">
      <Translate contentKey="global.menu.entities.prescricoes" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
