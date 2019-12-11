import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMedicamentos, defaultValue } from 'app/shared/model/medicamentos.model';

export const ACTION_TYPES = {
  SEARCH_MEDICAMENTOS: 'medicamentos/SEARCH_MEDICAMENTOS',
  FETCH_MEDICAMENTOS_LIST: 'medicamentos/FETCH_MEDICAMENTOS_LIST',
  FETCH_MEDICAMENTOS: 'medicamentos/FETCH_MEDICAMENTOS',
  CREATE_MEDICAMENTOS: 'medicamentos/CREATE_MEDICAMENTOS',
  UPDATE_MEDICAMENTOS: 'medicamentos/UPDATE_MEDICAMENTOS',
  DELETE_MEDICAMENTOS: 'medicamentos/DELETE_MEDICAMENTOS',
  RESET: 'medicamentos/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMedicamentos>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type MedicamentosState = Readonly<typeof initialState>;

// Reducer

export default (state: MedicamentosState = initialState, action): MedicamentosState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MEDICAMENTOS):
    case REQUEST(ACTION_TYPES.FETCH_MEDICAMENTOS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEDICAMENTOS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MEDICAMENTOS):
    case REQUEST(ACTION_TYPES.UPDATE_MEDICAMENTOS):
    case REQUEST(ACTION_TYPES.DELETE_MEDICAMENTOS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_MEDICAMENTOS):
    case FAILURE(ACTION_TYPES.FETCH_MEDICAMENTOS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEDICAMENTOS):
    case FAILURE(ACTION_TYPES.CREATE_MEDICAMENTOS):
    case FAILURE(ACTION_TYPES.UPDATE_MEDICAMENTOS):
    case FAILURE(ACTION_TYPES.DELETE_MEDICAMENTOS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MEDICAMENTOS):
    case SUCCESS(ACTION_TYPES.FETCH_MEDICAMENTOS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEDICAMENTOS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEDICAMENTOS):
    case SUCCESS(ACTION_TYPES.UPDATE_MEDICAMENTOS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEDICAMENTOS):
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

const apiUrl = 'api/medicamentos';
const apiSearchUrl = 'api/_search/medicamentos';

// Actions

export const getSearchEntities: ICrudSearchAction<IMedicamentos> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MEDICAMENTOS,
  payload: axios.get<IMedicamentos>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IMedicamentos> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MEDICAMENTOS_LIST,
  payload: axios.get<IMedicamentos>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IMedicamentos> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEDICAMENTOS,
    payload: axios.get<IMedicamentos>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMedicamentos> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEDICAMENTOS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMedicamentos> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEDICAMENTOS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMedicamentos> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEDICAMENTOS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
