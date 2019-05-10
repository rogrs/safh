import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './internacoes.reducer';
import { IInternacoes } from 'app/shared/model/internacoes.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInternacoesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class InternacoesDetail extends React.Component<IInternacoesDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { internacoesEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Internacoes [<b>{internacoesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="dataInternacao">Data Internacao</span>
            </dt>
            <dd>
              <TextFormat value={internacoesEntity.dataInternacao} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="descricao">Descricao</span>
            </dt>
            <dd>{internacoesEntity.descricao}</dd>
            <dt>Pacientes</dt>
            <dd>{internacoesEntity.pacientes ? internacoesEntity.pacientes.nome : ''}</dd>
            <dt>Clinicas</dt>
            <dd>{internacoesEntity.clinicas ? internacoesEntity.clinicas.clinica : ''}</dd>
            <dt>Medicos</dt>
            <dd>{internacoesEntity.medicos ? internacoesEntity.medicos.nome : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/internacoes" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/internacoes/${internacoesEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ internacoes }: IRootState) => ({
  internacoesEntity: internacoes.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InternacoesDetail);
