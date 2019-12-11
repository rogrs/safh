import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IInternacoes } from 'app/shared/model/internacoes.model';
import { getEntities as getInternacoes } from 'app/entities/internacoes/internacoes.reducer';
import { IDietas } from 'app/shared/model/dietas.model';
import { getEntities as getDietas } from 'app/entities/dietas/dietas.reducer';
import { IPrescricoes } from 'app/shared/model/prescricoes.model';
import { getEntities as getPrescricoes } from 'app/entities/prescricoes/prescricoes.reducer';
import { IPosologias } from 'app/shared/model/posologias.model';
import { getEntities as getPosologias } from 'app/entities/posologias/posologias.reducer';
import { getEntity, updateEntity, createEntity, reset } from './internacoes-detalhes.reducer';
import { IInternacoesDetalhes } from 'app/shared/model/internacoes-detalhes.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IInternacoesDetalhesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IInternacoesDetalhesUpdateState {
  isNew: boolean;
  internacoesId: string;
  dietasId: string;
  prescricoesId: string;
  posologiasId: string;
}

export class InternacoesDetalhesUpdate extends React.Component<IInternacoesDetalhesUpdateProps, IInternacoesDetalhesUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      internacoesId: '0',
      dietasId: '0',
      prescricoesId: '0',
      posologiasId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getInternacoes();
    this.props.getDietas();
    this.props.getPrescricoes();
    this.props.getPosologias();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { internacoesDetalhesEntity } = this.props;
      const entity = {
        ...internacoesDetalhesEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/internacoes-detalhes');
  };

  render() {
    const { internacoesDetalhesEntity, internacoes, dietas, prescricoes, posologias, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="safhApp.internacoesDetalhes.home.createOrEditLabel">
              <Translate contentKey="safhApp.internacoesDetalhes.home.createOrEditLabel">Create or edit a InternacoesDetalhes</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : internacoesDetalhesEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="internacoes-detalhes-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="internacoes-detalhes-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dataDetalheLabel" for="internacoes-detalhes-dataDetalhe">
                    <Translate contentKey="safhApp.internacoesDetalhes.dataDetalhe">Data Detalhe</Translate>
                  </Label>
                  <AvField
                    id="internacoes-detalhes-dataDetalhe"
                    type="date"
                    className="form-control"
                    name="dataDetalhe"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="horarioLabel" for="internacoes-detalhes-horario">
                    <Translate contentKey="safhApp.internacoesDetalhes.horario">Horario</Translate>
                  </Label>
                  <AvField
                    id="internacoes-detalhes-horario"
                    type="date"
                    className="form-control"
                    name="horario"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="qtdLabel" for="internacoes-detalhes-qtd">
                    <Translate contentKey="safhApp.internacoesDetalhes.qtd">Qtd</Translate>
                  </Label>
                  <AvField
                    id="internacoes-detalhes-qtd"
                    type="string"
                    className="form-control"
                    name="qtd"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="internacoes-detalhes-internacoes">
                    <Translate contentKey="safhApp.internacoesDetalhes.internacoes">Internacoes</Translate>
                  </Label>
                  <AvInput id="internacoes-detalhes-internacoes" type="select" className="form-control" name="internacoes.id">
                    <option value="" key="0" />
                    {internacoes
                      ? internacoes.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.descricao}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="internacoes-detalhes-dietas">
                    <Translate contentKey="safhApp.internacoesDetalhes.dietas">Dietas</Translate>
                  </Label>
                  <AvInput id="internacoes-detalhes-dietas" type="select" className="form-control" name="dietas.id">
                    <option value="" key="0" />
                    {dietas
                      ? dietas.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.dieta}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="internacoes-detalhes-prescricoes">
                    <Translate contentKey="safhApp.internacoesDetalhes.prescricoes">Prescricoes</Translate>
                  </Label>
                  <AvInput id="internacoes-detalhes-prescricoes" type="select" className="form-control" name="prescricoes.id">
                    <option value="" key="0" />
                    {prescricoes
                      ? prescricoes.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.prescricao}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="internacoes-detalhes-posologias">
                    <Translate contentKey="safhApp.internacoesDetalhes.posologias">Posologias</Translate>
                  </Label>
                  <AvInput id="internacoes-detalhes-posologias" type="select" className="form-control" name="posologias.id">
                    <option value="" key="0" />
                    {posologias
                      ? posologias.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.posologia}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/internacoes-detalhes" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  internacoes: storeState.internacoes.entities,
  dietas: storeState.dietas.entities,
  prescricoes: storeState.prescricoes.entities,
  posologias: storeState.posologias.entities,
  internacoesDetalhesEntity: storeState.internacoesDetalhes.entity,
  loading: storeState.internacoesDetalhes.loading,
  updating: storeState.internacoesDetalhes.updating,
  updateSuccess: storeState.internacoesDetalhes.updateSuccess
});

const mapDispatchToProps = {
  getInternacoes,
  getDietas,
  getPrescricoes,
  getPosologias,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InternacoesDetalhesUpdate);
