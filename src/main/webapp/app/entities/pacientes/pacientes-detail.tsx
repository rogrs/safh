import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './pacientes.reducer';
import { IPacientes } from 'app/shared/model/pacientes.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPacientesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PacientesDetail extends React.Component<IPacientesDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { pacientesEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Pacientes [<b>{pacientesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="prontuario">Prontuario</span>
            </dt>
            <dd>{pacientesEntity.prontuario}</dd>
            <dt>
              <span id="nome">Nome</span>
            </dt>
            <dd>{pacientesEntity.nome}</dd>
            <dt>
              <span id="cpf">Cpf</span>
            </dt>
            <dd>{pacientesEntity.cpf}</dd>
            <dt>
              <span id="email">Email</span>
            </dt>
            <dd>{pacientesEntity.email}</dd>
            <dt>
              <span id="cep">Cep</span>
            </dt>
            <dd>{pacientesEntity.cep}</dd>
            <dt>
              <span id="logradouro">Logradouro</span>
            </dt>
            <dd>{pacientesEntity.logradouro}</dd>
            <dt>
              <span id="numero">Numero</span>
            </dt>
            <dd>{pacientesEntity.numero}</dd>
            <dt>
              <span id="complemento">Complemento</span>
            </dt>
            <dd>{pacientesEntity.complemento}</dd>
            <dt>
              <span id="bairro">Bairro</span>
            </dt>
            <dd>{pacientesEntity.bairro}</dd>
            <dt>
              <span id="cidade">Cidade</span>
            </dt>
            <dd>{pacientesEntity.cidade}</dd>
            <dt>
              <span id="uF">U F</span>
            </dt>
            <dd>{pacientesEntity.uF}</dd>
            <dt>Clinicas</dt>
            <dd>{pacientesEntity.clinicas ? pacientesEntity.clinicas.clinica : ''}</dd>
            <dt>Enfermarias</dt>
            <dd>{pacientesEntity.enfermarias ? pacientesEntity.enfermarias.enfermaria : ''}</dd>
            <dt>Leitos</dt>
            <dd>{pacientesEntity.leitos ? pacientesEntity.leitos.leito : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/pacientes" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/pacientes/${pacientesEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ pacientes }: IRootState) => ({
  pacientesEntity: pacientes.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PacientesDetail);
