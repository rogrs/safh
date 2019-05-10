import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IEspecialidades } from 'app/shared/model/especialidades.model';
import { getEntities as getEspecialidades } from 'app/entities/especialidades/especialidades.reducer';
import { getEntity, updateEntity, createEntity, reset } from './medicos.reducer';
import { IMedicos } from 'app/shared/model/medicos.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMedicosUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMedicosUpdateState {
  isNew: boolean;
  especialidadesId: string;
}

export class MedicosUpdate extends React.Component<IMedicosUpdateProps, IMedicosUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      especialidadesId: '0',
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

    this.props.getEspecialidades();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { medicosEntity } = this.props;
      const entity = {
        ...medicosEntity,
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
    this.props.history.push('/entity/medicos');
  };

  render() {
    const { medicosEntity, especialidades, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="safhApp.medicos.home.createOrEditLabel">Create or edit a Medicos</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : medicosEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="medicos-id">ID</Label>
                    <AvInput id="medicos-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nomeLabel" for="medicos-nome">
                    Nome
                  </Label>
                  <AvField
                    id="medicos-nome"
                    type="text"
                    name="nome"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      maxLength: { value: 255, errorMessage: 'This field cannot be longer than 255 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="crmLabel" for="medicos-crm">
                    Crm
                  </Label>
                  <AvField
                    id="medicos-crm"
                    type="text"
                    name="crm"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      maxLength: { value: 40, errorMessage: 'This field cannot be longer than 40 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cpfLabel" for="medicos-cpf">
                    Cpf
                  </Label>
                  <AvField
                    id="medicos-cpf"
                    type="text"
                    name="cpf"
                    validate={{
                      maxLength: { value: 11, errorMessage: 'This field cannot be longer than 11 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="medicos-email">
                    Email
                  </Label>
                  <AvField
                    id="medicos-email"
                    type="text"
                    name="email"
                    validate={{
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cepLabel" for="medicos-cep">
                    Cep
                  </Label>
                  <AvField
                    id="medicos-cep"
                    type="text"
                    name="cep"
                    validate={{
                      maxLength: { value: 10, errorMessage: 'This field cannot be longer than 10 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="logradouroLabel" for="medicos-logradouro">
                    Logradouro
                  </Label>
                  <AvField
                    id="medicos-logradouro"
                    type="text"
                    name="logradouro"
                    validate={{
                      maxLength: { value: 80, errorMessage: 'This field cannot be longer than 80 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="numeroLabel" for="medicos-numero">
                    Numero
                  </Label>
                  <AvField
                    id="medicos-numero"
                    type="text"
                    name="numero"
                    validate={{
                      maxLength: { value: 10, errorMessage: 'This field cannot be longer than 10 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="complementoLabel" for="medicos-complemento">
                    Complemento
                  </Label>
                  <AvField
                    id="medicos-complemento"
                    type="text"
                    name="complemento"
                    validate={{
                      maxLength: { value: 60, errorMessage: 'This field cannot be longer than 60 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="bairroLabel" for="medicos-bairro">
                    Bairro
                  </Label>
                  <AvField
                    id="medicos-bairro"
                    type="text"
                    name="bairro"
                    validate={{
                      maxLength: { value: 60, errorMessage: 'This field cannot be longer than 60 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cidadeLabel" for="medicos-cidade">
                    Cidade
                  </Label>
                  <AvField
                    id="medicos-cidade"
                    type="text"
                    name="cidade"
                    validate={{
                      maxLength: { value: 60, errorMessage: 'This field cannot be longer than 60 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="uFLabel" for="medicos-uF">
                    U F
                  </Label>
                  <AvInput id="medicos-uF" type="select" className="form-control" name="uF" value={(!isNew && medicosEntity.uF) || 'AC'}>
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
                  <Label for="medicos-especialidades">Especialidades</Label>
                  <AvInput id="medicos-especialidades" type="select" className="form-control" name="especialidades.id">
                    <option value="" key="0" />
                    {especialidades
                      ? especialidades.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.especialidade}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/medicos" replace color="info">
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
  especialidades: storeState.especialidades.entities,
  medicosEntity: storeState.medicos.entity,
  loading: storeState.medicos.loading,
  updating: storeState.medicos.updating,
  updateSuccess: storeState.medicos.updateSuccess
});

const mapDispatchToProps = {
  getEspecialidades,
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
)(MedicosUpdate);
