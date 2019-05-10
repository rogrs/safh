import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IClinicas } from 'app/shared/model/clinicas.model';
import { getEntities as getClinicas } from 'app/entities/clinicas/clinicas.reducer';
import { IEnfermarias } from 'app/shared/model/enfermarias.model';
import { getEntities as getEnfermarias } from 'app/entities/enfermarias/enfermarias.reducer';
import { ILeitos } from 'app/shared/model/leitos.model';
import { getEntities as getLeitos } from 'app/entities/leitos/leitos.reducer';
import { getEntity, updateEntity, createEntity, reset } from './pacientes.reducer';
import { IPacientes } from 'app/shared/model/pacientes.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPacientesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPacientesUpdateState {
  isNew: boolean;
  clinicasId: string;
  enfermariasId: string;
  leitosId: string;
}

export class PacientesUpdate extends React.Component<IPacientesUpdateProps, IPacientesUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      clinicasId: '0',
      enfermariasId: '0',
      leitosId: '0',
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

    this.props.getClinicas();
    this.props.getEnfermarias();
    this.props.getLeitos();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { pacientesEntity } = this.props;
      const entity = {
        ...pacientesEntity,
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
    this.props.history.push('/entity/pacientes');
  };

  render() {
    const { pacientesEntity, clinicas, enfermarias, leitos, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="safhApp.pacientes.home.createOrEditLabel">Create or edit a Pacientes</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : pacientesEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="pacientes-id">ID</Label>
                    <AvInput id="pacientes-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="prontuarioLabel" for="pacientes-prontuario">
                    Prontuario
                  </Label>
                  <AvField
                    id="pacientes-prontuario"
                    type="string"
                    className="form-control"
                    name="prontuario"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nomeLabel" for="pacientes-nome">
                    Nome
                  </Label>
                  <AvField
                    id="pacientes-nome"
                    type="text"
                    name="nome"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      maxLength: { value: 255, errorMessage: 'This field cannot be longer than 255 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cpfLabel" for="pacientes-cpf">
                    Cpf
                  </Label>
                  <AvField
                    id="pacientes-cpf"
                    type="text"
                    name="cpf"
                    validate={{
                      maxLength: { value: 11, errorMessage: 'This field cannot be longer than 11 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="pacientes-email">
                    Email
                  </Label>
                  <AvField
                    id="pacientes-email"
                    type="text"
                    name="email"
                    validate={{
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cepLabel" for="pacientes-cep">
                    Cep
                  </Label>
                  <AvField
                    id="pacientes-cep"
                    type="text"
                    name="cep"
                    validate={{
                      maxLength: { value: 10, errorMessage: 'This field cannot be longer than 10 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="logradouroLabel" for="pacientes-logradouro">
                    Logradouro
                  </Label>
                  <AvField
                    id="pacientes-logradouro"
                    type="text"
                    name="logradouro"
                    validate={{
                      maxLength: { value: 80, errorMessage: 'This field cannot be longer than 80 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="numeroLabel" for="pacientes-numero">
                    Numero
                  </Label>
                  <AvField
                    id="pacientes-numero"
                    type="text"
                    name="numero"
                    validate={{
                      maxLength: { value: 10, errorMessage: 'This field cannot be longer than 10 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="complementoLabel" for="pacientes-complemento">
                    Complemento
                  </Label>
                  <AvField
                    id="pacientes-complemento"
                    type="text"
                    name="complemento"
                    validate={{
                      maxLength: { value: 60, errorMessage: 'This field cannot be longer than 60 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="bairroLabel" for="pacientes-bairro">
                    Bairro
                  </Label>
                  <AvField
                    id="pacientes-bairro"
                    type="text"
                    name="bairro"
                    validate={{
                      maxLength: { value: 60, errorMessage: 'This field cannot be longer than 60 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cidadeLabel" for="pacientes-cidade">
                    Cidade
                  </Label>
                  <AvField
                    id="pacientes-cidade"
                    type="text"
                    name="cidade"
                    validate={{
                      maxLength: { value: 60, errorMessage: 'This field cannot be longer than 60 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="uFLabel" for="pacientes-uF">
                    U F
                  </Label>
                  <AvInput
                    id="pacientes-uF"
                    type="select"
                    className="form-control"
                    name="uF"
                    value={(!isNew && pacientesEntity.uF) || 'AC'}
                  >
                    <option value="AC">AC</option>
                    <option value="AL">AL</option>
                    <option value="AM">AM</option>
                    <option value="AP">AP</option>
                    <option value="BA">BA</option>
                    <option value="CE">CE</option>
                    <option value="DF">DF</option>
                    <option value="ES">ES</option>
                    <option value="GO">GO</option>
                    <option value="MA">MA</option>
                    <option value="MG">MG</option>
                    <option value="MS">MS</option>
                    <option value="MT">MT</option>
                    <option value="PA">PA</option>
                    <option value="PB">PB</option>
                    <option value="PE">PE</option>
                    <option value="PI">PI</option>
                    <option value="PR">PR</option>
                    <option value="RJ">RJ</option>
                    <option value="RN">RN</option>
                    <option value="RO">RO</option>
                    <option value="RR">RR</option>
                    <option value="RS">RS</option>
                    <option value="SC">SC</option>
                    <option value="SE">SE</option>
                    <option value="SP">SP</option>
                    <option value="TO">TO</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="pacientes-clinicas">Clinicas</Label>
                  <AvInput id="pacientes-clinicas" type="select" className="form-control" name="clinicas.id">
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
                  <Label for="pacientes-enfermarias">Enfermarias</Label>
                  <AvInput id="pacientes-enfermarias" type="select" className="form-control" name="enfermarias.id">
                    <option value="" key="0" />
                    {enfermarias
                      ? enfermarias.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.enfermaria}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="pacientes-leitos">Leitos</Label>
                  <AvInput id="pacientes-leitos" type="select" className="form-control" name="leitos.id">
                    <option value="" key="0" />
                    {leitos
                      ? leitos.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.leito}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/pacientes" replace color="info">
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
  clinicas: storeState.clinicas.entities,
  enfermarias: storeState.enfermarias.entities,
  leitos: storeState.leitos.entities,
  pacientesEntity: storeState.pacientes.entity,
  loading: storeState.pacientes.loading,
  updating: storeState.pacientes.updating,
  updateSuccess: storeState.pacientes.updateSuccess
});

const mapDispatchToProps = {
  getClinicas,
  getEnfermarias,
  getLeitos,
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
)(PacientesUpdate);
