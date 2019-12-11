import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPacientes, defaultValue } from 'app/shared/model/pacientes.model';

export const ACTION_TYPES = {
  SEARCH_PACIENTES: 'pacientes/SEARCH_PACIENTES',
  FETCH_PACIENTES_LIST: 'pacientes/FETCH_PACIENTES_LIST',
  FETCH_PACIENTES: 'pacientes/FETCH_PACIENTES',
  CREATE_PACIENTES: 'pacientes/CREATE_PACIENTES',
  UPDATE_PACIENTES: 'pacientes/UPDATE_PACIENTES',
  DELETE_PACIENTES: 'pacientes/DELETE_PACIENTES',
  RESET: 'pacientes/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPacientes>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PacientesState = Readonly<typeof initialState>;

// Reducer

export default (state: PacientesState = initialState, action): PacientesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PACIENTES):
    case REQUEST(ACTION_TYPES.FETCH_PACIENTES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PACIENTES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PACIENTES):
    case REQUEST(ACTION_TYPES.UPDATE_PACIENTES):
    case REQUEST(ACTION_TYPES.DELETE_PACIENTES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_PACIENTES):
    case FAILURE(ACTION_TYPES.FETCH_PACIENTES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PACIENTES):
    case FAILURE(ACTION_TYPES.CREATE_PACIENTES):
    case FAILURE(ACTION_TYPES.UPDATE_PACIENTES):
    case FAILURE(ACTION_TYPES.DELETE_PACIENTES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PACIENTES):
    case SUCCESS(ACTION_TYPES.FETCH_PACIENTES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PACIENTES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PACIENTES):
    case SUCCESS(ACTION_TYPES.UPDATE_PACIENTES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PACIENTES):
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

const apiUrl = 'api/pacientes';
const apiSearchUrl = 'api/_search/pacientes';

// Actions

export const getSearchEntities: ICrudSearchAction<IPacientes> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_PACIENTES,
  payload: axios.get<IPacientes>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IPacientes> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PACIENTES_LIST,
  payload: axios.get<IPacientes>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPacientes> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PACIENTES,
    payload: axios.get<IPacientes>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPacientes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PACIENTES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPacientes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PACIENTES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPacientes> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PACIENTES,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
