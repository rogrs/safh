import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './medicos.reducer';
import { IMedicos } from 'app/shared/model/medicos.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMedicosDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MedicosDetail extends React.Component<IMedicosDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { medicosEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="safhApp.medicos.detail.title">Medicos</Translate> [<b>{medicosEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="nome">
                <Translate contentKey="safhApp.medicos.nome">Nome</Translate>
              </span>
            </dt>
            <dd>{medicosEntity.nome}</dd>
            <dt>
              <span id="crm">
                <Translate contentKey="safhApp.medicos.crm">Crm</Translate>
              </span>
            </dt>
            <dd>{medicosEntity.crm}</dd>
            <dt>
              <span id="cpf">
                <Translate contentKey="safhApp.medicos.cpf">Cpf</Translate>
              </span>
            </dt>
            <dd>{medicosEntity.cpf}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="safhApp.medicos.email">Email</Translate>
              </span>
            </dt>
            <dd>{medicosEntity.email}</dd>
            <dt>
              <span id="cep">
                <Translate contentKey="safhApp.medicos.cep">Cep</Translate>
              </span>
            </dt>
            <dd>{medicosEntity.cep}</dd>
            <dt>
              <span id="logradouro">
                <Translate contentKey="safhApp.medicos.logradouro">Logradouro</Translate>
              </span>
            </dt>
            <dd>{medicosEntity.logradouro}</dd>
            <dt>
              <span id="numero">
                <Translate contentKey="safhApp.medicos.numero">Numero</Translate>
              </span>
            </dt>
            <dd>{medicosEntity.numero}</dd>
            <dt>
              <span id="complemento">
                <Translate contentKey="safhApp.medicos.complemento">Complemento</Translate>
              </span>
            </dt>
            <dd>{medicosEntity.complemento}</dd>
            <dt>
              <span id="bairro">
                <Translate contentKey="safhApp.medicos.bairro">Bairro</Translate>
              </span>
            </dt>
            <dd>{medicosEntity.bairro}</dd>
            <dt>
              <span id="cidade">
                <Translate contentKey="safhApp.medicos.cidade">Cidade</Translate>
              </span>
            </dt>
            <dd>{medicosEntity.cidade}</dd>
            <dt>
              <span id="uF">
                <Translate contentKey="safhApp.medicos.uF">U F</Translate>
              </span>
            </dt>
            <dd>{medicosEntity.uF}</dd>
            <dt>
              <Translate contentKey="safhApp.medicos.especialidades">Especialidades</Translate>
            </dt>
            <dd>{medicosEntity.especialidades ? medicosEntity.especialidades.especialidade : ''}</dd>
          </dl>
          <Button tag={Link} to="/medicos" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/medicos/${medicosEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ medicos }: IRootState) => ({
  medicosEntity: medicos.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MedicosDetail);
