import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities, reset } from './internacoes-detalhes.reducer';
import { IInternacoesDetalhes } from 'app/shared/model/internacoes-detalhes.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IInternacoesDetalhesProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IInternacoesDetalhesState extends IPaginationBaseState {
  search: string;
}

export class InternacoesDetalhes extends React.Component<IInternacoesDetalhesProps, IInternacoesDetalhesState> {
  state: IInternacoesDetalhesState = {
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
    const { internacoesDetalhesList, match } = this.props;
    return (
      <div>
        <h2 id="internacoes-detalhes-heading">
          Internacoes Detalhes
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Internacoes Detalhes
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
                  <th className="hand" onClick={this.sort('dataDetalhe')}>
                    Data Detalhe <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('horario')}>
                    Horario <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('qtd')}>
                    Qtd <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Internacoes <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Dietas <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Prescricoes <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Posologias <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {internacoesDetalhesList.map((internacoesDetalhes, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${internacoesDetalhes.id}`} color="link" size="sm">
                        {internacoesDetalhes.id}
                      </Button>
                    </td>
                    <td>
                      <TextFormat type="date" value={internacoesDetalhes.dataDetalhe} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>
                      <TextFormat type="date" value={internacoesDetalhes.horario} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>{internacoesDetalhes.qtd}</td>
                    <td>
                      {internacoesDetalhes.internacoes ? (
                        <Link to={`internacoes/${internacoesDetalhes.internacoes.id}`}>{internacoesDetalhes.internacoes.descricao}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {internacoesDetalhes.dietas ? (
                        <Link to={`dietas/${internacoesDetalhes.dietas.id}`}>{internacoesDetalhes.dietas.dieta}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {internacoesDetalhes.prescricoes ? (
                        <Link to={`prescricoes/${internacoesDetalhes.prescricoes.id}`}>{internacoesDetalhes.prescricoes.prescricao}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {internacoesDetalhes.posologias ? (
                        <Link to={`posologias/${internacoesDetalhes.posologias.id}`}>{internacoesDetalhes.posologias.posologia}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${internacoesDetalhes.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${internacoesDetalhes.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${internacoesDetalhes.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ internacoesDetalhes }: IRootState) => ({
  internacoesDetalhesList: internacoesDetalhes.entities,
  totalItems: internacoesDetalhes.totalItems,
  links: internacoesDetalhes.links,
  entity: internacoesDetalhes.entity,
  updateSuccess: internacoesDetalhes.updateSuccess
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
)(InternacoesDetalhes);
