import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './internacoes-detalhes.reducer';
import { IInternacoesDetalhes } from 'app/shared/model/internacoes-detalhes.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInternacoesDetalhesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class InternacoesDetalhesDetail extends React.Component<IInternacoesDetalhesDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { internacoesDetalhesEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="safhApp.internacoesDetalhes.detail.title">InternacoesDetalhes</Translate> [
            <b>{internacoesDetalhesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="dataDetalhe">
                <Translate contentKey="safhApp.internacoesDetalhes.dataDetalhe">Data Detalhe</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={internacoesDetalhesEntity.dataDetalhe} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="horario">
                <Translate contentKey="safhApp.internacoesDetalhes.horario">Horario</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={internacoesDetalhesEntity.horario} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="qtd">
                <Translate contentKey="safhApp.internacoesDetalhes.qtd">Qtd</Translate>
              </span>
            </dt>
            <dd>{internacoesDetalhesEntity.qtd}</dd>
            <dt>
              <Translate contentKey="safhApp.internacoesDetalhes.internacoes">Internacoes</Translate>
            </dt>
            <dd>{internacoesDetalhesEntity.internacoes ? internacoesDetalhesEntity.internacoes.descricao : ''}</dd>
            <dt>
              <Translate contentKey="safhApp.internacoesDetalhes.dietas">Dietas</Translate>
            </dt>
            <dd>{internacoesDetalhesEntity.dietas ? internacoesDetalhesEntity.dietas.dieta : ''}</dd>
            <dt>
              <Translate contentKey="safhApp.internacoesDetalhes.prescricoes">Prescricoes</Translate>
            </dt>
            <dd>{internacoesDetalhesEntity.prescricoes ? internacoesDetalhesEntity.prescricoes.prescricao : ''}</dd>
            <dt>
              <Translate contentKey="safhApp.internacoesDetalhes.posologias">Posologias</Translate>
            </dt>
            <dd>{internacoesDetalhesEntity.posologias ? internacoesDetalhesEntity.posologias.posologia : ''}</dd>
          </dl>
          <Button tag={Link} to="/internacoes-detalhes" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/internacoes-detalhes/${internacoesDetalhesEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ internacoesDetalhes }: IRootState) => ({
  internacoesDetalhesEntity: internacoesDetalhes.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InternacoesDetalhesDetail);
