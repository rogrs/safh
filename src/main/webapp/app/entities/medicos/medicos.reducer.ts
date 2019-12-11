import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMedicos, defaultValue } from 'app/shared/model/medicos.model';

export const ACTION_TYPES = {
  SEARCH_MEDICOS: 'medicos/SEARCH_MEDICOS',
  FETCH_MEDICOS_LIST: 'medicos/FETCH_MEDICOS_LIST',
  FETCH_MEDICOS: 'medicos/FETCH_MEDICOS',
  CREATE_MEDICOS: 'medicos/CREATE_MEDICOS',
  UPDATE_MEDICOS: 'medicos/UPDATE_MEDICOS',
  DELETE_MEDICOS: 'medicos/DELETE_MEDICOS',
  RESET: 'medicos/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMedicos>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type MedicosState = Readonly<typeof initialState>;

// Reducer

export default (state: MedicosState = initialState, action): MedicosState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MEDICOS):
    case REQUEST(ACTION_TYPES.FETCH_MEDICOS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEDICOS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MEDICOS):
    case REQUEST(ACTION_TYPES.UPDATE_MEDICOS):
    case REQUEST(ACTION_TYPES.DELETE_MEDICOS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_MEDICOS):
    case FAILURE(ACTION_TYPES.FETCH_MEDICOS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEDICOS):
    case FAILURE(ACTION_TYPES.CREATE_MEDICOS):
    case FAILURE(ACTION_TYPES.UPDATE_MEDICOS):
    case FAILURE(ACTION_TYPES.DELETE_MEDICOS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MEDICOS):
    case SUCCESS(ACTION_TYPES.FETCH_MEDICOS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEDICOS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEDICOS):
    case SUCCESS(ACTION_TYPES.UPDATE_MEDICOS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEDICOS):
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

const apiUrl = 'api/medicos';
const apiSearchUrl = 'api/_search/medicos';

// Actions

export const getSearchEntities: ICrudSearchAction<IMedicos> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MEDICOS,
  payload: axios.get<IMedicos>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IMedicos> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MEDICOS_LIST,
  payload: axios.get<IMedicos>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IMedicos> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEDICOS,
    payload: axios.get<IMedicos>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMedicos> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEDICOS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMedicos> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEDICOS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMedicos> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEDICOS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
