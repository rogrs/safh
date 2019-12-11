import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './leitos.reducer';
import { ILeitos } from 'app/shared/model/leitos.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILeitosDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class LeitosDetail extends React.Component<ILeitosDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { leitosEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="safhApp.leitos.detail.title">Leitos</Translate> [<b>{leitosEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="leito">
                <Translate contentKey="safhApp.leitos.leito">Leito</Translate>
              </span>
            </dt>
            <dd>{leitosEntity.leito}</dd>
            <dt>
              <span id="tipo">
                <Translate contentKey="safhApp.leitos.tipo">Tipo</Translate>
              </span>
            </dt>
            <dd>{leitosEntity.tipo}</dd>
          </dl>
          <Button tag={Link} to="/leitos" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/leitos/${leitosEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ leitos }: IRootState) => ({
  leitosEntity: leitos.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LeitosDetail);
