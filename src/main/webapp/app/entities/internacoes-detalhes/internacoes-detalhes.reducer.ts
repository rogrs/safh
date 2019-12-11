import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInternacoesDetalhes, defaultValue } from 'app/shared/model/internacoes-detalhes.model';

export const ACTION_TYPES = {
  SEARCH_INTERNACOESDETALHES: 'internacoesDetalhes/SEARCH_INTERNACOESDETALHES',
  FETCH_INTERNACOESDETALHES_LIST: 'internacoesDetalhes/FETCH_INTERNACOESDETALHES_LIST',
  FETCH_INTERNACOESDETALHES: 'internacoesDetalhes/FETCH_INTERNACOESDETALHES',
  CREATE_INTERNACOESDETALHES: 'internacoesDetalhes/CREATE_INTERNACOESDETALHES',
  UPDATE_INTERNACOESDETALHES: 'internacoesDetalhes/UPDATE_INTERNACOESDETALHES',
  DELETE_INTERNACOESDETALHES: 'internacoesDetalhes/DELETE_INTERNACOESDETALHES',
  RESET: 'internacoesDetalhes/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInternacoesDetalhes>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type InternacoesDetalhesState = Readonly<typeof initialState>;

// Reducer

export default (state: InternacoesDetalhesState = initialState, action): InternacoesDetalhesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_INTERNACOESDETALHES):
    case REQUEST(ACTION_TYPES.FETCH_INTERNACOESDETALHES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INTERNACOESDETALHES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_INTERNACOESDETALHES):
    case REQUEST(ACTION_TYPES.UPDATE_INTERNACOESDETALHES):
    case REQUEST(ACTION_TYPES.DELETE_INTERNACOESDETALHES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_INTERNACOESDETALHES):
    case FAILURE(ACTION_TYPES.FETCH_INTERNACOESDETALHES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INTERNACOESDETALHES):
    case FAILURE(ACTION_TYPES.CREATE_INTERNACOESDETALHES):
    case FAILURE(ACTION_TYPES.UPDATE_INTERNACOESDETALHES):
    case FAILURE(ACTION_TYPES.DELETE_INTERNACOESDETALHES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_INTERNACOESDETALHES):
    case SUCCESS(ACTION_TYPES.FETCH_INTERNACOESDETALHES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_INTERNACOESDETALHES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_INTERNACOESDETALHES):
    case SUCCESS(ACTION_TYPES.UPDATE_INTERNACOESDETALHES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_INTERNACOESDETALHES):
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

const apiUrl = 'api/internacoes-detalhes';
const apiSearchUrl = 'api/_search/internacoes-detalhes';

// Actions

export const getSearchEntities: ICrudSearchAction<IInternacoesDetalhes> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_INTERNACOESDETALHES,
  payload: axios.get<IInternacoesDetalhes>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IInternacoesDetalhes> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_INTERNACOESDETALHES_LIST,
  payload: axios.get<IInternacoesDetalhes>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IInternacoesDetalhes> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INTERNACOESDETALHES,
    payload: axios.get<IInternacoesDetalhes>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IInternacoesDetalhes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INTERNACOESDETALHES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IInternacoesDetalhes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INTERNACOESDETALHES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInternacoesDetalhes> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INTERNACOESDETALHES,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
