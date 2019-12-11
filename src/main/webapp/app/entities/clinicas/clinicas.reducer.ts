import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IClinicas, defaultValue } from 'app/shared/model/clinicas.model';

export const ACTION_TYPES = {
  SEARCH_CLINICAS: 'clinicas/SEARCH_CLINICAS',
  FETCH_CLINICAS_LIST: 'clinicas/FETCH_CLINICAS_LIST',
  FETCH_CLINICAS: 'clinicas/FETCH_CLINICAS',
  CREATE_CLINICAS: 'clinicas/CREATE_CLINICAS',
  UPDATE_CLINICAS: 'clinicas/UPDATE_CLINICAS',
  DELETE_CLINICAS: 'clinicas/DELETE_CLINICAS',
  RESET: 'clinicas/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IClinicas>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ClinicasState = Readonly<typeof initialState>;

// Reducer

export default (state: ClinicasState = initialState, action): ClinicasState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CLINICAS):
    case REQUEST(ACTION_TYPES.FETCH_CLINICAS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CLINICAS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CLINICAS):
    case REQUEST(ACTION_TYPES.UPDATE_CLINICAS):
    case REQUEST(ACTION_TYPES.DELETE_CLINICAS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_CLINICAS):
    case FAILURE(ACTION_TYPES.FETCH_CLINICAS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CLINICAS):
    case FAILURE(ACTION_TYPES.CREATE_CLINICAS):
    case FAILURE(ACTION_TYPES.UPDATE_CLINICAS):
    case FAILURE(ACTION_TYPES.DELETE_CLINICAS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CLINICAS):
    case SUCCESS(ACTION_TYPES.FETCH_CLINICAS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CLINICAS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CLINICAS):
    case SUCCESS(ACTION_TYPES.UPDATE_CLINICAS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CLINICAS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/clinicas';
const apiSearchUrl = 'api/_search/clinicas';

// Actions

export const getSearchEntities: ICrudSearchAction<IClinicas> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_CLINICAS,
  payload: axios.get<IClinicas>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IClinicas> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CLINICAS_LIST,
  payload: axios.get<IClinicas>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IClinicas> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CLINICAS,
    payload: axios.get<IClinicas>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IClinicas> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CLINICAS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IClinicas> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CLINICAS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IClinicas> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CLINICAS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
