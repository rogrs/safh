import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './enfermarias.reducer';
import { IEnfermarias } from 'app/shared/model/enfermarias.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEnfermariasDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class EnfermariasDetail extends React.Component<IEnfermariasDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { enfermariasEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Enfermarias [<b>{enfermariasEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="enfermaria">Enfermaria</span>
            </dt>
            <dd>{enfermariasEntity.enfermaria}</dd>
          </dl>
          <Button tag={Link} to="/entity/enfermarias" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/enfermarias/${enfermariasEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ enfermarias }: IRootState) => ({
  enfermariasEntity: enfermarias.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EnfermariasDetail);
