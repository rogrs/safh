import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudSearchAction, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './pacientes.reducer';
import { IPacientes } from 'app/shared/model/pacientes.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPacientesProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IPacientesState {
  search: string;
}

export class Pacientes extends React.Component<IPacientesProps, IPacientesState> {
  state: IPacientesState = {
    search: ''
  };

  componentDidMount() {
    this.props.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.setState({ search: '' }, () => {
      this.props.getEntities();
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  render() {
    const { pacientesList, match } = this.props;
    return (
      <div>
        <h2 id="pacientes-heading">
          <Translate contentKey="safhApp.pacientes.home.title">Pacientes</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="safhApp.pacientes.home.createLabel">Create a new Pacientes</Translate>
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput
                    type="text"
                    name="search"
                    value={this.state.search}
                    onChange={this.handleSearch}
                    placeholder={translate('safhApp.pacientes.home.search')}
                  />
                  <Button className="input-group-addon">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          {pacientesList && pacientesList.length > 0 ? (
            <Table responsive aria-describedby="pacientes-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.prontuario">Prontuario</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.nome">Nome</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.cpf">Cpf</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.email">Email</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.cep">Cep</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.logradouro">Logradouro</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.numero">Numero</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.complemento">Complemento</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.bairro">Bairro</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.cidade">Cidade</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.uF">U F</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.clinicas">Clinicas</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.enfermarias">Enfermarias</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.pacientes.leitos">Leitos</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {pacientesList.map((pacientes, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${pacientes.id}`} color="link" size="sm">
                        {pacientes.id}
                      </Button>
                    </td>
                    <td>{pacientes.prontuario}</td>
                    <td>{pacientes.nome}</td>
                    <td>{pacientes.cpf}</td>
                    <td>{pacientes.email}</td>
                    <td>{pacientes.cep}</td>
                    <td>{pacientes.logradouro}</td>
                    <td>{pacientes.numero}</td>
                    <td>{pacientes.complemento}</td>
                    <td>{pacientes.bairro}</td>
                    <td>{pacientes.cidade}</td>
                    <td>
                      <Translate contentKey={`safhApp.Estados.${pacientes.uF}`} />
                    </td>
                    <td>{pacientes.clinicas ? <Link to={`clinicas/${pacientes.clinicas.id}`}>{pacientes.clinicas.clinica}</Link> : ''}</td>
                    <td>
                      {pacientes.enfermarias ? (
                        <Link to={`enfermarias/${pacientes.enfermarias.id}`}>{pacientes.enfermarias.enfermaria}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>{pacientes.leitos ? <Link to={`leitos/${pacientes.leitos.id}`}>{pacientes.leitos.leito}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${pacientes.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${pacientes.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${pacientes.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="safhApp.pacientes.home.notFound">No Pacientes found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ pacientes }: IRootState) => ({
  pacientesList: pacientes.entities
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Pacientes);
