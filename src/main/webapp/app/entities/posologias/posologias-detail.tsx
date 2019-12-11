import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './posologias.reducer';
import { IPosologias } from 'app/shared/model/posologias.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPosologiasDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PosologiasDetail extends React.Component<IPosologiasDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { posologiasEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="safhApp.posologias.detail.title">Posologias</Translate> [<b>{posologiasEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="posologia">
                <Translate contentKey="safhApp.posologias.posologia">Posologia</Translate>
              </span>
            </dt>
            <dd>{posologiasEntity.posologia}</dd>
          </dl>
          <Button tag={Link} to="/posologias" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/posologias/${posologiasEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ posologias }: IRootState) => ({
  posologiasEntity: posologias.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PosologiasDetail);
