import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './medicamentos.reducer';
import { IMedicamentos } from 'app/shared/model/medicamentos.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMedicamentosDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MedicamentosDetail extends React.Component<IMedicamentosDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { medicamentosEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Medicamentos [<b>{medicamentosEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="descricao">Descricao</span>
            </dt>
            <dd>{medicamentosEntity.descricao}</dd>
            <dt>
              <span id="registroMinisterioSaude">Registro Ministerio Saude</span>
            </dt>
            <dd>{medicamentosEntity.registroMinisterioSaude}</dd>
            <dt>
              <span id="codigoBarras">Codigo Barras</span>
            </dt>
            <dd>{medicamentosEntity.codigoBarras}</dd>
            <dt>
              <span id="qtdAtual">Qtd Atual</span>
            </dt>
            <dd>{medicamentosEntity.qtdAtual}</dd>
            <dt>
              <span id="qtdMin">Qtd Min</span>
            </dt>
            <dd>{medicamentosEntity.qtdMin}</dd>
            <dt>
              <span id="qtdMax">Qtd Max</span>
            </dt>
            <dd>{medicamentosEntity.qtdMax}</dd>
            <dt>
              <span id="observacoes">Observacoes</span>
            </dt>
            <dd>{medicamentosEntity.observacoes}</dd>
            <dt>
              <span id="apresentacao">Apresentacao</span>
            </dt>
            <dd>{medicamentosEntity.apresentacao}</dd>
            <dt>Posologia Padrao</dt>
            <dd>{medicamentosEntity.posologiaPadrao ? medicamentosEntity.posologiaPadrao.posologia : ''}</dd>
            <dt>Fabricantes</dt>
            <dd>{medicamentosEntity.fabricantes ? medicamentosEntity.fabricantes.fabricante : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/medicamentos" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/medicamentos/${medicamentosEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ medicamentos }: IRootState) => ({
  medicamentosEntity: medicamentos.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MedicamentosDetail);
