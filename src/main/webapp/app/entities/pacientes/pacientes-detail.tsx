import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './pacientes.reducer';
import { IPacientes } from 'app/shared/model/pacientes.model';
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
            <Translate contentKey="safhApp.pacientes.detail.title">Pacientes</Translate> [<b>{pacientesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="prontuario">
                <Translate contentKey="safhApp.pacientes.prontuario">Prontuario</Translate>
              </span>
            </dt>
            <dd>{pacientesEntity.prontuario}</dd>
            <dt>
              <span id="nome">
                <Translate contentKey="safhApp.pacientes.nome">Nome</Translate>
              </span>
            </dt>
            <dd>{pacientesEntity.nome}</dd>
            <dt>
              <span id="cpf">
                <Translate contentKey="safhApp.pacientes.cpf">Cpf</Translate>
              </span>
            </dt>
            <dd>{pacientesEntity.cpf}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="safhApp.pacientes.email">Email</Translate>
              </span>
            </dt>
            <dd>{pacientesEntity.email}</dd>
            <dt>
              <span id="cep">
                <Translate contentKey="safhApp.pacientes.cep">Cep</Translate>
              </span>
            </dt>
            <dd>{pacientesEntity.cep}</dd>
            <dt>
              <span id="logradouro">
                <Translate contentKey="safhApp.pacientes.logradouro">Logradouro</Translate>
              </span>
            </dt>
            <dd>{pacientesEntity.logradouro}</dd>
            <dt>
              <span id="numero">
                <Translate contentKey="safhApp.pacientes.numero">Numero</Translate>
              </span>
            </dt>
            <dd>{pacientesEntity.numero}</dd>
            <dt>
              <span id="complemento">
                <Translate contentKey="safhApp.pacientes.complemento">Complemento</Translate>
              </span>
            </dt>
            <dd>{pacientesEntity.complemento}</dd>
            <dt>
              <span id="bairro">
                <Translate contentKey="safhApp.pacientes.bairro">Bairro</Translate>
              </span>
            </dt>
            <dd>{pacientesEntity.bairro}</dd>
            <dt>
              <span id="cidade">
                <Translate contentKey="safhApp.pacientes.cidade">Cidade</Translate>
              </span>
            </dt>
            <dd>{pacientesEntity.cidade}</dd>
            <dt>
              <span id="uF">
                <Translate contentKey="safhApp.pacientes.uF">U F</Translate>
              </span>
            </dt>
            <dd>{pacientesEntity.uF}</dd>
            <dt>
              <Translate contentKey="safhApp.pacientes.clinicas">Clinicas</Translate>
            </dt>
            <dd>{pacientesEntity.clinicas ? pacientesEntity.clinicas.clinica : ''}</dd>
            <dt>
              <Translate contentKey="safhApp.pacientes.enfermarias">Enfermarias</Translate>
            </dt>
            <dd>{pacientesEntity.enfermarias ? pacientesEntity.enfermarias.enfermaria : ''}</dd>
            <dt>
              <Translate contentKey="safhApp.pacientes.leitos">Leitos</Translate>
            </dt>
            <dd>{pacientesEntity.leitos ? pacientesEntity.leitos.leito : ''}</dd>
          </dl>
          <Button tag={Link} to="/pacientes" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/pacientes/${pacientesEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
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
