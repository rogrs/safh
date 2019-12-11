import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './dietas.reducer';
import { IDietas } from 'app/shared/model/dietas.model';
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
            <Translate contentKey="safhApp.dietas.detail.title">Dietas</Translate> [<b>{dietasEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="dieta">
                <Translate contentKey="safhApp.dietas.dieta">Dieta</Translate>
              </span>
            </dt>
            <dd>{dietasEntity.dieta}</dd>
            <dt>
              <span id="descricao">
                <Translate contentKey="safhApp.dietas.descricao">Descricao</Translate>
              </span>
            </dt>
            <dd>{dietasEntity.descricao}</dd>
          </dl>
          <Button tag={Link} to="/dietas" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/dietas/${dietasEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
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
