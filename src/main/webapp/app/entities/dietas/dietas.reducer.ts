import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDietas, defaultValue } from 'app/shared/model/dietas.model';

export const ACTION_TYPES = {
  SEARCH_DIETAS: 'dietas/SEARCH_DIETAS',
  FETCH_DIETAS_LIST: 'dietas/FETCH_DIETAS_LIST',
  FETCH_DIETAS: 'dietas/FETCH_DIETAS',
  CREATE_DIETAS: 'dietas/CREATE_DIETAS',
  UPDATE_DIETAS: 'dietas/UPDATE_DIETAS',
  DELETE_DIETAS: 'dietas/DELETE_DIETAS',
  RESET: 'dietas/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDietas>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type DietasState = Readonly<typeof initialState>;

// Reducer

export default (state: DietasState = initialState, action): DietasState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_DIETAS):
    case REQUEST(ACTION_TYPES.FETCH_DIETAS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DIETAS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DIETAS):
    case REQUEST(ACTION_TYPES.UPDATE_DIETAS):
    case REQUEST(ACTION_TYPES.DELETE_DIETAS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_DIETAS):
    case FAILURE(ACTION_TYPES.FETCH_DIETAS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DIETAS):
    case FAILURE(ACTION_TYPES.CREATE_DIETAS):
    case FAILURE(ACTION_TYPES.UPDATE_DIETAS):
    case FAILURE(ACTION_TYPES.DELETE_DIETAS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_DIETAS):
    case SUCCESS(ACTION_TYPES.FETCH_DIETAS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DIETAS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DIETAS):
    case SUCCESS(ACTION_TYPES.UPDATE_DIETAS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DIETAS):
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

const apiUrl = 'api/dietas';
const apiSearchUrl = 'api/_search/dietas';

// Actions

export const getSearchEntities: ICrudSearchAction<IDietas> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_DIETAS,
  payload: axios.get<IDietas>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IDietas> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_DIETAS_LIST,
  payload: axios.get<IDietas>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IDietas> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DIETAS,
    payload: axios.get<IDietas>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDietas> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DIETAS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDietas> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DIETAS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDietas> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DIETAS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
