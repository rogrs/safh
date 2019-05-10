import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './prescricoes.reducer';
import { IPrescricoes } from 'app/shared/model/prescricoes.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPrescricoesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PrescricoesDetail extends React.Component<IPrescricoesDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { prescricoesEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Prescricoes [<b>{prescricoesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="prescricao">Prescricao</span>
            </dt>
            <dd>{prescricoesEntity.prescricao}</dd>
          </dl>
          <Button tag={Link} to="/entity/prescricoes" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/prescricoes/${prescricoesEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ prescricoes }: IRootState) => ({
  prescricoesEntity: prescricoes.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PrescricoesDetail);
