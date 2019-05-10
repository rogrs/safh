import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPacientes } from 'app/shared/model/pacientes.model';
import { getEntities as getPacientes } from 'app/entities/pacientes/pacientes.reducer';
import { IClinicas } from 'app/shared/model/clinicas.model';
import { getEntities as getClinicas } from 'app/entities/clinicas/clinicas.reducer';
import { IMedicos } from 'app/shared/model/medicos.model';
import { getEntities as getMedicos } from 'app/entities/medicos/medicos.reducer';
import { getEntity, updateEntity, createEntity, reset } from './internacoes.reducer';
import { IInternacoes } from 'app/shared/model/internacoes.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IInternacoesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IInternacoesUpdateState {
  isNew: boolean;
  pacientesId: string;
  clinicasId: string;
  medicosId: string;
}

export class InternacoesUpdate extends React.Component<IInternacoesUpdateProps, IInternacoesUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      pacientesId: '0',
      clinicasId: '0',
      medicosId: '0',
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

    this.props.getPacientes();
    this.props.getClinicas();
    this.props.getMedicos();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { internacoesEntity } = this.props;
      const entity = {
        ...internacoesEntity,
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
    this.props.history.push('/entity/internacoes');
  };

  render() {
    const { internacoesEntity, pacientes, clinicas, medicos, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="safhApp.internacoes.home.createOrEditLabel">Create or edit a Internacoes</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : internacoesEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="internacoes-id">ID</Label>
                    <AvInput id="internacoes-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dataInternacaoLabel" for="internacoes-dataInternacao">
                    Data Internacao
                  </Label>
                  <AvField
                    id="internacoes-dataInternacao"
                    type="date"
                    className="form-control"
                    name="dataInternacao"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descricaoLabel" for="internacoes-descricao">
                    Descricao
                  </Label>
                  <AvField
                    id="internacoes-descricao"
                    type="text"
                    name="descricao"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      maxLength: { value: 200, errorMessage: 'This field cannot be longer than 200 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="internacoes-pacientes">Pacientes</Label>
                  <AvInput id="internacoes-pacientes" type="select" className="form-control" name="pacientes.id">
                    <option value="" key="0" />
                    {pacientes
                      ? pacientes.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.nome}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="internacoes-clinicas">Clinicas</Label>
                  <AvInput id="internacoes-clinicas" type="select" className="form-control" name="clinicas.id">
                    <option value="" key="0" />
                    {clinicas
                      ? clinicas.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.clinica}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="internacoes-medicos">Medicos</Label>
                  <AvInput id="internacoes-medicos" type="select" className="form-control" name="medicos.id">
                    <option value="" key="0" />
                    {medicos
                      ? medicos.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.nome}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/internacoes" replace color="info">
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
  pacientes: storeState.pacientes.entities,
  clinicas: storeState.clinicas.entities,
  medicos: storeState.medicos.entities,
  internacoesEntity: storeState.internacoes.entity,
  loading: storeState.internacoes.loading,
  updating: storeState.internacoes.updating,
  updateSuccess: storeState.internacoes.updateSuccess
});

const mapDispatchToProps = {
  getPacientes,
  getClinicas,
  getMedicos,
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
)(InternacoesUpdate);
