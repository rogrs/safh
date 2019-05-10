import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './fabricantes.reducer';
import { IFabricantes } from 'app/shared/model/fabricantes.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFabricantesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FabricantesDetail extends React.Component<IFabricantesDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { fabricantesEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Fabricantes [<b>{fabricantesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="fabricante">Fabricante</span>
            </dt>
            <dd>{fabricantesEntity.fabricante}</dd>
          </dl>
          <Button tag={Link} to="/entity/fabricantes" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/fabricantes/${fabricantesEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ fabricantes }: IRootState) => ({
  fabricantesEntity: fabricantes.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FabricantesDetail);
