import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPosologias } from 'app/shared/model/posologias.model';
import { getEntities as getPosologias } from 'app/entities/posologias/posologias.reducer';
import { IFabricantes } from 'app/shared/model/fabricantes.model';
import { getEntities as getFabricantes } from 'app/entities/fabricantes/fabricantes.reducer';
import { getEntity, updateEntity, createEntity, reset } from './medicamentos.reducer';
import { IMedicamentos } from 'app/shared/model/medicamentos.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMedicamentosUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMedicamentosUpdateState {
  isNew: boolean;
  posologiaPadraoId: string;
  fabricantesId: string;
}

export class MedicamentosUpdate extends React.Component<IMedicamentosUpdateProps, IMedicamentosUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      posologiaPadraoId: '0',
      fabricantesId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getPosologias();
    this.props.getFabricantes();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { medicamentosEntity } = this.props;
      const entity = {
        ...medicamentosEntity,
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
    this.props.history.push('/entity/medicamentos');
  };

  render() {
    const { medicamentosEntity, posologias, fabricantes, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="safhApp.medicamentos.home.createOrEditLabel">Create or edit a Medicamentos</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : medicamentosEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="medicamentos-id">ID</Label>
                    <AvInput id="medicamentos-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="descricaoLabel" for="medicamentos-descricao">
                    Descricao
                  </Label>
                  <AvField
                    id="medicamentos-descricao"
                    type="text"
                    name="descricao"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="registroMinisterioSaudeLabel" for="medicamentos-registroMinisterioSaude">
                    Registro Ministerio Saude
                  </Label>
                  <AvField
                    id="medicamentos-registroMinisterioSaude"
                    type="text"
                    name="registroMinisterioSaude"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      maxLength: { value: 60, errorMessage: 'This field cannot be longer than 60 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="codigoBarrasLabel" for="medicamentos-codigoBarras">
                    Codigo Barras
                  </Label>
                  <AvField
                    id="medicamentos-codigoBarras"
                    type="text"
                    name="codigoBarras"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      maxLength: { value: 13, errorMessage: 'This field cannot be longer than 13 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="qtdAtualLabel" for="medicamentos-qtdAtual">
                    Qtd Atual
                  </Label>
                  <AvField id="medicamentos-qtdAtual" type="string" className="form-control" name="qtdAtual" />
                </AvGroup>
                <AvGroup>
                  <Label id="qtdMinLabel" for="medicamentos-qtdMin">
                    Qtd Min
                  </Label>
                  <AvField id="medicamentos-qtdMin" type="string" className="form-control" name="qtdMin" />
                </AvGroup>
                <AvGroup>
                  <Label id="qtdMaxLabel" for="medicamentos-qtdMax">
                    Qtd Max
                  </Label>
                  <AvField id="medicamentos-qtdMax" type="string" className="form-control" name="qtdMax" />
                </AvGroup>
                <AvGroup>
                  <Label id="observacoesLabel" for="medicamentos-observacoes">
                    Observacoes
                  </Label>
                  <AvField
                    id="medicamentos-observacoes"
                    type="text"
                    name="observacoes"
                    validate={{
                      maxLength: { value: 8000, errorMessage: 'This field cannot be longer than 8000 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="apresentacaoLabel" for="medicamentos-apresentacao">
                    Apresentacao
                  </Label>
                  <AvField id="medicamentos-apresentacao" type="text" name="apresentacao" />
                </AvGroup>
                <AvGroup>
                  <Label for="medicamentos-posologiaPadrao">Posologia Padrao</Label>
                  <AvInput id="medicamentos-posologiaPadrao" type="select" className="form-control" name="posologiaPadrao.id">
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
                <AvGroup>
                  <Label for="medicamentos-fabricantes">Fabricantes</Label>
                  <AvInput id="medicamentos-fabricantes" type="select" className="form-control" name="fabricantes.id">
                    <option value="" key="0" />
                    {fabricantes
                      ? fabricantes.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.fabricante}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/medicamentos" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
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
  posologias: storeState.posologias.entities,
  fabricantes: storeState.fabricantes.entities,
  medicamentosEntity: storeState.medicamentos.entity,
  loading: storeState.medicamentos.loading,
  updating: storeState.medicamentos.updating,
  updateSuccess: storeState.medicamentos.updateSuccess
});

const mapDispatchToProps = {
  getPosologias,
  getFabricantes,
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
)(MedicamentosUpdate);
