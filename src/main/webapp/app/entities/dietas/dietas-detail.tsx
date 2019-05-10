import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './dietas.reducer';
import { IDietas } from 'app/shared/model/dietas.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDietasDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DietasDetail extends React.Component<IDietasDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { dietasEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Dietas [<b>{dietasEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="dieta">Dieta</span>
            </dt>
            <dd>{dietasEntity.dieta}</dd>
            <dt>
              <span id="descricao">Descricao</span>
            </dt>
            <dd>{dietasEntity.descricao}</dd>
          </dl>
          <Button tag={Link} to="/entity/dietas" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/dietas/${dietasEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ dietas }: IRootState) => ({
  dietasEntity: dietas.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DietasDetail);
