import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IEspecialidades } from 'app/shared/model/especialidades.model';
import { getEntities as getEspecialidades } from 'app/entities/especialidades/especialidades.reducer';
import { getEntity, updateEntity, createEntity, reset } from './medicos.reducer';
import { IMedicos } from 'app/shared/model/medicos.model';
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
    if (this.state.isNew) {
      this.props.reset();
    } else {
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
    this.props.history.push('/medicos');
  };

  render() {
    const { medicosEntity, especialidades, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="safhApp.medicos.home.createOrEditLabel">
              <Translate contentKey="safhApp.medicos.home.createOrEditLabel">Create or edit a Medicos</Translate>
            </h2>
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
                    <Label for="medicos-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="medicos-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nomeLabel" for="medicos-nome">
                    <Translate contentKey="safhApp.medicos.nome">Nome</Translate>
                  </Label>
                  <AvField
                    id="medicos-nome"
                    type="text"
                    name="nome"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="crmLabel" for="medicos-crm">
                    <Translate contentKey="safhApp.medicos.crm">Crm</Translate>
                  </Label>
                  <AvField
                    id="medicos-crm"
                    type="text"
                    name="crm"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 40, errorMessage: translate('entity.validation.maxlength', { max: 40 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cpfLabel" for="medicos-cpf">
                    <Translate contentKey="safhApp.medicos.cpf">Cpf</Translate>
                  </Label>
                  <AvField
                    id="medicos-cpf"
                    type="text"
                    name="cpf"
                    validate={{
                      maxLength: { value: 11, errorMessage: translate('entity.validation.maxlength', { max: 11 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="medicos-email">
                    <Translate contentKey="safhApp.medicos.email">Email</Translate>
                  </Label>
                  <AvField
                    id="medicos-email"
                    type="text"
                    name="email"
                    validate={{
                      maxLength: { value: 100, errorMessage: translate('entity.validation.maxlength', { max: 100 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cepLabel" for="medicos-cep">
                    <Translate contentKey="safhApp.medicos.cep">Cep</Translate>
                  </Label>
                  <AvField
                    id="medicos-cep"
                    type="text"
                    name="cep"
                    validate={{
                      maxLength: { value: 10, errorMessage: translate('entity.validation.maxlength', { max: 10 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="logradouroLabel" for="medicos-logradouro">
                    <Translate contentKey="safhApp.medicos.logradouro">Logradouro</Translate>
                  </Label>
                  <AvField
                    id="medicos-logradouro"
                    type="text"
                    name="logradouro"
                    validate={{
                      maxLength: { value: 80, errorMessage: translate('entity.validation.maxlength', { max: 80 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="numeroLabel" for="medicos-numero">
                    <Translate contentKey="safhApp.medicos.numero">Numero</Translate>
                  </Label>
                  <AvField
                    id="medicos-numero"
                    type="text"
                    name="numero"
                    validate={{
                      maxLength: { value: 10, errorMessage: translate('entity.validation.maxlength', { max: 10 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="complementoLabel" for="medicos-complemento">
                    <Translate contentKey="safhApp.medicos.complemento">Complemento</Translate>
                  </Label>
                  <AvField
                    id="medicos-complemento"
                    type="text"
                    name="complemento"
                    validate={{
                      maxLength: { value: 60, errorMessage: translate('entity.validation.maxlength', { max: 60 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="bairroLabel" for="medicos-bairro">
                    <Translate contentKey="safhApp.medicos.bairro">Bairro</Translate>
                  </Label>
                  <AvField
                    id="medicos-bairro"
                    type="text"
                    name="bairro"
                    validate={{
                      maxLength: { value: 60, errorMessage: translate('entity.validation.maxlength', { max: 60 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="cidadeLabel" for="medicos-cidade">
                    <Translate contentKey="safhApp.medicos.cidade">Cidade</Translate>
                  </Label>
                  <AvField
                    id="medicos-cidade"
                    type="text"
                    name="cidade"
                    validate={{
                      maxLength: { value: 60, errorMessage: translate('entity.validation.maxlength', { max: 60 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="uFLabel" for="medicos-uF">
                    <Translate contentKey="safhApp.medicos.uF">U F</Translate>
                  </Label>
                  <AvInput id="medicos-uF" type="select" className="form-control" name="uF" value={(!isNew && medicosEntity.uF) || 'AC'}>
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
                  <Label for="medicos-especialidades">
                    <Translate contentKey="safhApp.medicos.especialidades">Especialidades</Translate>
                  </Label>
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
                <Button tag={Link} id="cancel-save" to="/medicos" replace color="info">
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
