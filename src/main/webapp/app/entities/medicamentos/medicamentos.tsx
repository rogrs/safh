import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudSearchAction, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './medicamentos.reducer';
import { IMedicamentos } from 'app/shared/model/medicamentos.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMedicamentosProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IMedicamentosState {
  search: string;
}

export class Medicamentos extends React.Component<IMedicamentosProps, IMedicamentosState> {
  state: IMedicamentosState = {
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
    const { medicamentosList, match } = this.props;
    return (
      <div>
        <h2 id="medicamentos-heading">
          <Translate contentKey="safhApp.medicamentos.home.title">Medicamentos</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="safhApp.medicamentos.home.createLabel">Create a new Medicamentos</Translate>
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
                    placeholder={translate('safhApp.medicamentos.home.search')}
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
          {medicamentosList && medicamentosList.length > 0 ? (
            <Table responsive aria-describedby="medicamentos-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.medicamentos.descricao">Descricao</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.medicamentos.registroMinisterioSaude">Registro Ministerio Saude</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.medicamentos.codigoBarras">Codigo Barras</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.medicamentos.qtdAtual">Qtd Atual</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.medicamentos.qtdMin">Qtd Min</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.medicamentos.qtdMax">Qtd Max</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.medicamentos.observacoes">Observacoes</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.medicamentos.apresentacao">Apresentacao</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.medicamentos.posologiaPadrao">Posologia Padrao</Translate>
                  </th>
                  <th>
                    <Translate contentKey="safhApp.medicamentos.fabricantes">Fabricantes</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {medicamentosList.map((medicamentos, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${medicamentos.id}`} color="link" size="sm">
                        {medicamentos.id}
                      </Button>
                    </td>
                    <td>{medicamentos.descricao}</td>
                    <td>{medicamentos.registroMinisterioSaude}</td>
                    <td>{medicamentos.codigoBarras}</td>
                    <td>{medicamentos.qtdAtual}</td>
                    <td>{medicamentos.qtdMin}</td>
                    <td>{medicamentos.qtdMax}</td>
                    <td>{medicamentos.observacoes}</td>
                    <td>{medicamentos.apresentacao}</td>
                    <td>
                      {medicamentos.posologiaPadrao ? (
                        <Link to={`posologias/${medicamentos.posologiaPadrao.id}`}>{medicamentos.posologiaPadrao.posologia}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {medicamentos.fabricantes ? (
                        <Link to={`fabricantes/${medicamentos.fabricantes.id}`}>{medicamentos.fabricantes.fabricante}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${medicamentos.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${medicamentos.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${medicamentos.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="safhApp.medicamentos.home.notFound">No Medicamentos found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ medicamentos }: IRootState) => ({
  medicamentosList: medicamentos.entities
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
)(Medicamentos);
