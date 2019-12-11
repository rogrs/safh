import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './medicamentos.reducer';
import { IMedicamentos } from 'app/shared/model/medicamentos.model';
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
            <Translate contentKey="safhApp.medicamentos.detail.title">Medicamentos</Translate> [<b>{medicamentosEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="descricao">
                <Translate contentKey="safhApp.medicamentos.descricao">Descricao</Translate>
              </span>
            </dt>
            <dd>{medicamentosEntity.descricao}</dd>
            <dt>
              <span id="registroMinisterioSaude">
                <Translate contentKey="safhApp.medicamentos.registroMinisterioSaude">Registro Ministerio Saude</Translate>
              </span>
            </dt>
            <dd>{medicamentosEntity.registroMinisterioSaude}</dd>
            <dt>
              <span id="codigoBarras">
                <Translate contentKey="safhApp.medicamentos.codigoBarras">Codigo Barras</Translate>
              </span>
            </dt>
            <dd>{medicamentosEntity.codigoBarras}</dd>
            <dt>
              <span id="qtdAtual">
                <Translate contentKey="safhApp.medicamentos.qtdAtual">Qtd Atual</Translate>
              </span>
            </dt>
            <dd>{medicamentosEntity.qtdAtual}</dd>
            <dt>
              <span id="qtdMin">
                <Translate contentKey="safhApp.medicamentos.qtdMin">Qtd Min</Translate>
              </span>
            </dt>
            <dd>{medicamentosEntity.qtdMin}</dd>
            <dt>
              <span id="qtdMax">
                <Translate contentKey="safhApp.medicamentos.qtdMax">Qtd Max</Translate>
              </span>
            </dt>
            <dd>{medicamentosEntity.qtdMax}</dd>
            <dt>
              <span id="observacoes">
                <Translate contentKey="safhApp.medicamentos.observacoes">Observacoes</Translate>
              </span>
            </dt>
            <dd>{medicamentosEntity.observacoes}</dd>
            <dt>
              <span id="apresentacao">
                <Translate contentKey="safhApp.medicamentos.apresentacao">Apresentacao</Translate>
              </span>
            </dt>
            <dd>{medicamentosEntity.apresentacao}</dd>
            <dt>
              <Translate contentKey="safhApp.medicamentos.posologiaPadrao">Posologia Padrao</Translate>
            </dt>
            <dd>{medicamentosEntity.posologiaPadrao ? medicamentosEntity.posologiaPadrao.posologia : ''}</dd>
            <dt>
              <Translate contentKey="safhApp.medicamentos.fabricantes">Fabricantes</Translate>
            </dt>
            <dd>{medicamentosEntity.fabricantes ? medicamentosEntity.fabricantes.fabricante : ''}</dd>
          </dl>
          <Button tag={Link} to="/medicamentos" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/medicamentos/${medicamentosEntity.id}/edit`} replace color="primary">
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
