import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './clinicas.reducer';
import { IClinicas } from 'app/shared/model/clinicas.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IClinicasDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ClinicasDetail extends React.Component<IClinicasDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { clinicasEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="safhApp.clinicas.detail.title">Clinicas</Translate> [<b>{clinicasEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="clinica">
                <Translate contentKey="safhApp.clinicas.clinica">Clinica</Translate>
              </span>
            </dt>
            <dd>{clinicasEntity.clinica}</dd>
            <dt>
              <span id="descricao">
                <Translate contentKey="safhApp.clinicas.descricao">Descricao</Translate>
              </span>
            </dt>
            <dd>{clinicasEntity.descricao}</dd>
          </dl>
          <Button tag={Link} to="/clinicas" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/clinicas/${clinicasEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ clinicas }: IRootState) => ({
  clinicasEntity: clinicas.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ClinicasDetail);
