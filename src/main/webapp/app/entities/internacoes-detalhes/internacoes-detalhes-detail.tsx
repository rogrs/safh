import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './internacoes-detalhes.reducer';
import { IInternacoesDetalhes } from 'app/shared/model/internacoes-detalhes.model';
// tslint:disable-next-line:no-unused-variable
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
            InternacoesDetalhes [<b>{internacoesDetalhesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="dataDetalhe">Data Detalhe</span>
            </dt>
            <dd>
              <TextFormat value={internacoesDetalhesEntity.dataDetalhe} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="horario">Horario</span>
            </dt>
            <dd>
              <TextFormat value={internacoesDetalhesEntity.horario} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="qtd">Qtd</span>
            </dt>
            <dd>{internacoesDetalhesEntity.qtd}</dd>
            <dt>Internacoes</dt>
            <dd>{internacoesDetalhesEntity.internacoes ? internacoesDetalhesEntity.internacoes.descricao : ''}</dd>
            <dt>Dietas</dt>
            <dd>{internacoesDetalhesEntity.dietas ? internacoesDetalhesEntity.dietas.dieta : ''}</dd>
            <dt>Prescricoes</dt>
            <dd>{internacoesDetalhesEntity.prescricoes ? internacoesDetalhesEntity.prescricoes.prescricao : ''}</dd>
            <dt>Posologias</dt>
            <dd>{internacoesDetalhesEntity.posologias ? internacoesDetalhesEntity.posologias.posologia : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/internacoes-detalhes" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/internacoes-detalhes/${internacoesDetalhesEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
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
