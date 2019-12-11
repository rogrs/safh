import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './especialidades.reducer';
import { IEspecialidades } from 'app/shared/model/especialidades.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEspecialidadesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class EspecialidadesDetail extends React.Component<IEspecialidadesDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { especialidadesEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="safhApp.especialidades.detail.title">Especialidades</Translate> [<b>{especialidadesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="especialidade">
                <Translate contentKey="safhApp.especialidades.especialidade">Especialidade</Translate>
              </span>
            </dt>
            <dd>{especialidadesEntity.especialidade}</dd>
          </dl>
          <Button tag={Link} to="/especialidades" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/especialidades/${especialidadesEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ especialidades }: IRootState) => ({
  especialidadesEntity: especialidades.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EspecialidadesDetail);
