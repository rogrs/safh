import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPrescricoes, defaultValue } from 'app/shared/model/prescricoes.model';

export const ACTION_TYPES = {
  SEARCH_PRESCRICOES: 'prescricoes/SEARCH_PRESCRICOES',
  FETCH_PRESCRICOES_LIST: 'prescricoes/FETCH_PRESCRICOES_LIST',
  FETCH_PRESCRICOES: 'prescricoes/FETCH_PRESCRICOES',
  CREATE_PRESCRICOES: 'prescricoes/CREATE_PRESCRICOES',
  UPDATE_PRESCRICOES: 'prescricoes/UPDATE_PRESCRICOES',
  DELETE_PRESCRICOES: 'prescricoes/DELETE_PRESCRICOES',
  RESET: 'prescricoes/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPrescricoes>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PrescricoesState = Readonly<typeof initialState>;

// Reducer

export default (state: PrescricoesState = initialState, action): PrescricoesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PRESCRICOES):
    case REQUEST(ACTION_TYPES.FETCH_PRESCRICOES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRESCRICOES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PRESCRICOES):
    case REQUEST(ACTION_TYPES.UPDATE_PRESCRICOES):
    case REQUEST(ACTION_TYPES.DELETE_PRESCRICOES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_PRESCRICOES):
    case FAILURE(ACTION_TYPES.FETCH_PRESCRICOES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRESCRICOES):
    case FAILURE(ACTION_TYPES.CREATE_PRESCRICOES):
    case FAILURE(ACTION_TYPES.UPDATE_PRESCRICOES):
    case FAILURE(ACTION_TYPES.DELETE_PRESCRICOES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PRESCRICOES):
    case SUCCESS(ACTION_TYPES.FETCH_PRESCRICOES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRESCRICOES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRESCRICOES):
    case SUCCESS(ACTION_TYPES.UPDATE_PRESCRICOES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRESCRICOES):
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

const apiUrl = 'api/prescricoes';
const apiSearchUrl = 'api/_search/prescricoes';

// Actions

export const getSearchEntities: ICrudSearchAction<IPrescricoes> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_PRESCRICOES,
  payload: axios.get<IPrescricoes>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IPrescricoes> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PRESCRICOES_LIST,
  payload: axios.get<IPrescricoes>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPrescricoes> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRESCRICOES,
    payload: axios.get<IPrescricoes>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPrescricoes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRESCRICOES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPrescricoes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRESCRICOES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPrescricoes> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRESCRICOES,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
