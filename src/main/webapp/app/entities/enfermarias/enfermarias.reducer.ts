import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEnfermarias, defaultValue } from 'app/shared/model/enfermarias.model';

export const ACTION_TYPES = {
  SEARCH_ENFERMARIAS: 'enfermarias/SEARCH_ENFERMARIAS',
  FETCH_ENFERMARIAS_LIST: 'enfermarias/FETCH_ENFERMARIAS_LIST',
  FETCH_ENFERMARIAS: 'enfermarias/FETCH_ENFERMARIAS',
  CREATE_ENFERMARIAS: 'enfermarias/CREATE_ENFERMARIAS',
  UPDATE_ENFERMARIAS: 'enfermarias/UPDATE_ENFERMARIAS',
  DELETE_ENFERMARIAS: 'enfermarias/DELETE_ENFERMARIAS',
  RESET: 'enfermarias/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEnfermarias>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type EnfermariasState = Readonly<typeof initialState>;

// Reducer

export default (state: EnfermariasState = initialState, action): EnfermariasState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ENFERMARIAS):
    case REQUEST(ACTION_TYPES.FETCH_ENFERMARIAS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ENFERMARIAS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ENFERMARIAS):
    case REQUEST(ACTION_TYPES.UPDATE_ENFERMARIAS):
    case REQUEST(ACTION_TYPES.DELETE_ENFERMARIAS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ENFERMARIAS):
    case FAILURE(ACTION_TYPES.FETCH_ENFERMARIAS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ENFERMARIAS):
    case FAILURE(ACTION_TYPES.CREATE_ENFERMARIAS):
    case FAILURE(ACTION_TYPES.UPDATE_ENFERMARIAS):
    case FAILURE(ACTION_TYPES.DELETE_ENFERMARIAS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ENFERMARIAS):
    case SUCCESS(ACTION_TYPES.FETCH_ENFERMARIAS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ENFERMARIAS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ENFERMARIAS):
    case SUCCESS(ACTION_TYPES.UPDATE_ENFERMARIAS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ENFERMARIAS):
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

const apiUrl = 'api/enfermarias';
const apiSearchUrl = 'api/_search/enfermarias';

// Actions

export const getSearchEntities: ICrudSearchAction<IEnfermarias> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ENFERMARIAS,
  payload: axios.get<IEnfermarias>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IEnfermarias> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ENFERMARIAS_LIST,
  payload: axios.get<IEnfermarias>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IEnfermarias> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ENFERMARIAS,
    payload: axios.get<IEnfermarias>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEnfermarias> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ENFERMARIAS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEnfermarias> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ENFERMARIAS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEnfermarias> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ENFERMARIAS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
