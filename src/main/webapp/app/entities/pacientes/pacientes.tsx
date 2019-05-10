import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities, reset } from './pacientes.reducer';
import { IPacientes } from 'app/shared/model/pacientes.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IPacientesProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IPacientesState extends IPaginationBaseState {
  search: string;
}

export class Pacientes extends React.Component<IPacientesProps, IPacientesState> {
  state: IPacientesState = {
    search: '',
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  search = () => {
    if (this.state.search) {
      this.props.reset();
      this.setState({ activePage: 1 }, () => {
        const { activePage, itemsPerPage, sort, order, search } = this.state;
        this.props.getSearchEntities(search, activePage - 1, itemsPerPage, `${sort},${order}`);
      });
    }
  };

  clear = () => {
    this.props.reset();
    this.setState({ search: '', activePage: 1 }, () => {
      this.props.getEntities();
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order, search } = this.state;
    if (search) {
      this.props.getSearchEntities(search, activePage - 1, itemsPerPage, `${sort},${order}`);
    } else {
      this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
    }
  };

  render() {
    const { pacientesList, match } = this.props;
    return (
      <div>
        <h2 id="pacientes-heading">
          Pacientes
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Pacientes
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput type="text" name="search" value={this.state.search} onChange={this.handleSearch} placeholder="Search" />
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
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    ID <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('prontuario')}>
                    Prontuario <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('nome')}>
                    Nome <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('cpf')}>
                    Cpf <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('email')}>
                    Email <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('cep')}>
                    Cep <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('logradouro')}>
                    Logradouro <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('numero')}>
                    Numero <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('complemento')}>
                    Complemento <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('bairro')}>
                    Bairro <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('cidade')}>
                    Cidade <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('uF')}>
                    U F <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Clinicas <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Enfermarias <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Leitos <FontAwesomeIcon icon="sort" />
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
                    <td>{pacientes.uF}</td>
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
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${pacientes.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${pacientes.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ pacientes }: IRootState) => ({
  pacientesList: pacientes.entities,
  totalItems: pacientes.totalItems,
  links: pacientes.links,
  entity: pacientes.entity,
  updateSuccess: pacientes.updateSuccess
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Pacientes);
