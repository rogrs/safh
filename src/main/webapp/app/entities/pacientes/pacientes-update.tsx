import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
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
    if (this.state.isNew) {
      this.props.reset();
    } else {
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
    this.props.history.push('/pacientes');
  };

  render() {
    const { pacientesEntity, clinicas, enfermarias, leitos, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="safhApp.pacientes.home.createOrEditLabel">
              <Translate contentKey="safhApp.pacientes.home.createOrEditLabel">Create or edit a Pacientes</Translate>
            </h2>
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
                    <Label for="pacientes-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="pacientes-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="prontuarioLabel" for="pacientes-prontuario">
                    <Translate contentKey="safhApp.pacientes.prontuario">Prontuario</Translate>
                  </Label>
                  <AvField
                    id="pacientes-prontuario"
                    type="string"
                    className="form-control"
                    name="prontuario"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nomeLabel" for="pacientes-nome">
                    <Translate contentKey="safhApp.pacientes.nome">Nome</Translate>
                  </Label>
                  <AvField
                    id="pacientes-nome"
                    type="text"
                    name="nome"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cpfLabel" for="pacientes-cpf">
                    <Translate contentKey="safhApp.pacientes.cpf">Cpf</Translate>
                  </Label>
                  <AvField
                    id="pacientes-cpf"
                    type="text"
                    name="cpf"
                    validate={{
                      maxLength: { value: 11, errorMessage: translate('entity.validation.maxlength', { max: 11 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="pacientes-email">
                    <Translate contentKey="safhApp.pacientes.email">Email</Translate>
                  </Label>
                  <AvField
                    id="pacientes-email"
                    type="text"
                    name="email"
                    validate={{
                      maxLength: { value: 100, errorMessage: translate('entity.validation.maxlength', { max: 100 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cepLabel" for="pacientes-cep">
                    <Translate contentKey="safhApp.pacientes.cep">Cep</Translate>
                  </Label>
                  <AvField
                    id="pacientes-cep"
                    type="text"
                    name="cep"
                    validate={{
                      maxLength: { value: 10, errorMessage: translate('entity.validation.maxlength', { max: 10 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="logradouroLabel" for="pacientes-logradouro">
                    <Translate contentKey="safhApp.pacientes.logradouro">Logradouro</Translate>
                  </Label>
                  <AvField
                    id="pacientes-logradouro"
                    type="text"
                    name="logradouro"
                    validate={{
                      maxLength: { value: 80, errorMessage: translate('entity.validation.maxlength', { max: 80 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="numeroLabel" for="pacientes-numero">
                    <Translate contentKey="safhApp.pacientes.numero">Numero</Translate>
                  </Label>
                  <AvField
                    id="pacientes-numero"
                    type="text"
                    name="numero"
                    validate={{
                      maxLength: { value: 10, errorMessage: translate('entity.validation.maxlength', { max: 10 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="complementoLabel" for="pacientes-complemento">
                    <Translate contentKey="safhApp.pacientes.complemento">Complemento</Translate>
                  </Label>
                  <AvField
                    id="pacientes-complemento"
                    type="text"
                    name="complemento"
                    validate={{
                      maxLength: { value: 60, errorMessage: translate('entity.validation.maxlength', { max: 60 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="bairroLabel" for="pacientes-bairro">
                    <Translate contentKey="safhApp.pacientes.bairro">Bairro</Translate>
                  </Label>
                  <AvField
                    id="pacientes-bairro"
                    type="text"
                    name="bairro"
                    validate={{
                      maxLength: { value: 60, errorMessage: translate('entity.validation.maxlength', { max: 60 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cidadeLabel" for="pacientes-cidade">
                    <Translate contentKey="safhApp.pacientes.cidade">Cidade</Translate>
                  </Label>
                  <AvField
                    id="pacientes-cidade"
                    type="text"
                    name="cidade"
                    validate={{
                      maxLength: { value: 60, errorMessage: translate('entity.validation.maxlength', { max: 60 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="uFLabel" for="pacientes-uF">
                    <Translate contentKey="safhApp.pacientes.uF">U F</Translate>
                  </Label>
                  <AvInput
                    id="pacientes-uF"
                    type="select"
                    className="form-control"
                    name="uF"
                    value={(!isNew && pacientesEntity.uF) || 'AC'}
                  >
                    <option value="AC">{translate('safhApp.Estados.AC')}</option>
                    <option value="AL">{translate('safhApp.Estados.AL')}</option>
                    <option value="AM">{translate('safhApp.Estados.AM')}</option>
                    <option value="AP">{translate('safhApp.Estados.AP')}</option>
                    <option value="BA">{translate('safhApp.Estados.BA')}</option>
                    <option value="CE">{translate('safhApp.Estados.CE')}</option>
                    <option value="DF">{translate('safhApp.Estados.DF')}</option>
                    <option value="ES">{translate('safhApp.Estados.ES')}</option>
                    <option value="GO">{translate('safhApp.Estados.GO')}</option>
                    <option value="MA">{translate('safhApp.Estados.MA')}</option>
                    <option value="MG">{translate('safhApp.Estados.MG')}</option>
                    <option value="MS">{translate('safhApp.Estados.MS')}</option>
                    <option value="MT">{translate('safhApp.Estados.MT')}</option>
                    <option value="PA">{translate('safhApp.Estados.PA')}</option>
                    <option value="PB">{translate('safhApp.Estados.PB')}</option>
                    <option value="PE">{translate('safhApp.Estados.PE')}</option>
                    <option value="PI">{translate('safhApp.Estados.PI')}</option>
                    <option value="PR">{translate('safhApp.Estados.PR')}</option>
                    <option value="RJ">{translate('safhApp.Estados.RJ')}</option>
                    <option value="RN">{translate('safhApp.Estados.RN')}</option>
                    <option value="RO">{translate('safhApp.Estados.RO')}</option>
                    <option value="RR">{translate('safhApp.Estados.RR')}</option>
                    <option value="RS">{translate('safhApp.Estados.RS')}</option>
                    <option value="SC">{translate('safhApp.Estados.SC')}</option>
                    <option value="SE">{translate('safhApp.Estados.SE')}</option>
                    <option value="SP">{translate('safhApp.Estados.SP')}</option>
                    <option value="TO">{translate('safhApp.Estados.TO')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="pacientes-clinicas">
                    <Translate contentKey="safhApp.pacientes.clinicas">Clinicas</Translate>
                  </Label>
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
                  <Label for="pacientes-enfermarias">
                    <Translate contentKey="safhApp.pacientes.enfermarias">Enfermarias</Translate>
                  </Label>
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
                  <Label for="pacientes-leitos">
                    <Translate contentKey="safhApp.pacientes.leitos">Leitos</Translate>
                  </Label>
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
                <Button tag={Link} id="cancel-save" to="/pacientes" replace color="info">
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
