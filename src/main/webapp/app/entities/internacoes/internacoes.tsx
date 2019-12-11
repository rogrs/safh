import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudSearchAction, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './internacoes.reducer';
import { IInternacoes } from 'app/shared/model/internacoes.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInternacoesProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IInternacoesState {
  search: string;
}

export class Internacoes extends React.Component<IInternacoesProps, IInternacoesState> {
  state: IInternacoesState = {
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
    const { internacoesList, match } = this.props;
    return (
      <div>
        <h2 id="internacoes-heading">
          <Translate contentKey="safhApp.internacoes.home.title">Internacoes</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="safhApp.internacoes.home.createLabel">Create a new Internacoes</Translate>
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
                    placeholder={translate('safhApp.internacoes.home.search')}
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
          {internacoesList && internacoesList.length > 0 ? (
            <Table responsive aria-describedby="internacoes-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.internacoes.dataInternacao">Data Internacao</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.internacoes.descricao">Descricao</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.internacoes.pacientes">Pacientes</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.internacoes.clinicas">Clinicas</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.internacoes.medicos">Medicos</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {internacoesList.map((internacoes, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${internacoes.id}`} color="link" size="sm">
                        {internacoes.id}
                      </Button>
                    </td>
                    <td>
                      <TextFormat type="date" value={internacoes.dataInternacao} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>{internacoes.descricao}</td>
                    <td>
                      {internacoes.pacientes ? <Link to={`pacientes/${internacoes.pacientes.id}`}>{internacoes.pacientes.nome}</Link> : ''}
                    </td>
                    <td>
                      {internacoes.clinicas ? <Link to={`clinicas/${internacoes.clinicas.id}`}>{internacoes.clinicas.clinica}</Link> : ''}
                    </td>
                    <td>{internacoes.medicos ? <Link to={`medicos/${internacoes.medicos.id}`}>{internacoes.medicos.nome}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${internacoes.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${internacoes.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${internacoes.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="safhApp.internacoes.home.notFound">No Internacoes found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ internacoes }: IRootState) => ({
  internacoesList: internacoes.entities
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
)(Internacoes);
