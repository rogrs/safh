import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudSearchAction, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './internacoes-detalhes.reducer';
import { IInternacoesDetalhes } from 'app/shared/model/internacoes-detalhes.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInternacoesDetalhesProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IInternacoesDetalhesState {
  search: string;
}

export class InternacoesDetalhes extends React.Component<IInternacoesDetalhesProps, IInternacoesDetalhesState> {
  state: IInternacoesDetalhesState = {
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
    const { internacoesDetalhesList, match } = this.props;
    return (
      <div>
        <h2 id="internacoes-detalhes-heading">
          <Translate contentKey="safhApp.internacoesDetalhes.home.title">Internacoes Detalhes</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="safhApp.internacoesDetalhes.home.createLabel">Create a new Internacoes Detalhes</Translate>
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
                    placeholder={translate('safhApp.internacoesDetalhes.home.search')}
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
          {internacoesDetalhesList && internacoesDetalhesList.length > 0 ? (
            <Table responsive aria-describedby="internacoes-detalhes-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.internacoesDetalhes.dataDetalhe">Data Detalhe</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.internacoesDetalhes.horario">Horario</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.internacoesDetalhes.qtd">Qtd</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.internacoesDetalhes.internacoes">Internacoes</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.internacoesDetalhes.dietas">Dietas</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.internacoesDetalhes.prescricoes">Prescricoes</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.internacoesDetalhes.posologias">Posologias</Translate>
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
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${internacoesDetalhes.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${internacoesDetalhes.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="safhApp.internacoesDetalhes.home.notFound">No Internacoes Detalhes found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ internacoesDetalhes }: IRootState) => ({
  internacoesDetalhesList: internacoesDetalhes.entities
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
)(InternacoesDetalhes);
