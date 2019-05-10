import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './medicos.reducer';
import { IMedicos } from 'app/shared/model/medicos.model';
// tslint:disable-next-line:no-unused-variable
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
            Medicos [<b>{medicosEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="nome">Nome</span>
            </dt>
            <dd>{medicosEntity.nome}</dd>
            <dt>
              <span id="crm">Crm</span>
            </dt>
            <dd>{medicosEntity.crm}</dd>
            <dt>
              <span id="cpf">Cpf</span>
            </dt>
            <dd>{medicosEntity.cpf}</dd>
            <dt>
              <span id="email">Email</span>
            </dt>
            <dd>{medicosEntity.email}</dd>
            <dt>
              <span id="cep">Cep</span>
            </dt>
            <dd>{medicosEntity.cep}</dd>
            <dt>
              <span id="logradouro">Logradouro</span>
            </dt>
            <dd>{medicosEntity.logradouro}</dd>
            <dt>
              <span id="numero">Numero</span>
            </dt>
            <dd>{medicosEntity.numero}</dd>
            <dt>
              <span id="complemento">Complemento</span>
            </dt>
            <dd>{medicosEntity.complemento}</dd>
            <dt>
              <span id="bairro">Bairro</span>
            </dt>
            <dd>{medicosEntity.bairro}</dd>
            <dt>
              <span id="cidade">Cidade</span>
            </dt>
            <dd>{medicosEntity.cidade}</dd>
            <dt>
              <span id="uF">U F</span>
            </dt>
            <dd>{medicosEntity.uF}</dd>
            <dt>Especialidades</dt>
            <dd>{medicosEntity.especialidades ? medicosEntity.especialidades.especialidade : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/medicos" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/medicos/${medicosEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
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
