import axios from 'axios';
import {
  ICrudSearchAction,
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInternacoes, defaultValue } from 'app/shared/model/internacoes.model';

export const ACTION_TYPES = {
  SEARCH_INTERNACOES: 'internacoes/SEARCH_INTERNACOES',
  FETCH_INTERNACOES_LIST: 'internacoes/FETCH_INTERNACOES_LIST',
  FETCH_INTERNACOES: 'internacoes/FETCH_INTERNACOES',
  CREATE_INTERNACOES: 'internacoes/CREATE_INTERNACOES',
  UPDATE_INTERNACOES: 'internacoes/UPDATE_INTERNACOES',
  DELETE_INTERNACOES: 'internacoes/DELETE_INTERNACOES',
  RESET: 'internacoes/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInternacoes>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type InternacoesState = Readonly<typeof initialState>;

// Reducer

export default (state: InternacoesState = initialState, action): InternacoesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_INTERNACOES):
    case REQUEST(ACTION_TYPES.FETCH_INTERNACOES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INTERNACOES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_INTERNACOES):
    case REQUEST(ACTION_TYPES.UPDATE_INTERNACOES):
    case REQUEST(ACTION_TYPES.DELETE_INTERNACOES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_INTERNACOES):
    case FAILURE(ACTION_TYPES.FETCH_INTERNACOES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INTERNACOES):
    case FAILURE(ACTION_TYPES.CREATE_INTERNACOES):
    case FAILURE(ACTION_TYPES.UPDATE_INTERNACOES):
    case FAILURE(ACTION_TYPES.DELETE_INTERNACOES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_INTERNACOES):
    case SUCCESS(ACTION_TYPES.FETCH_INTERNACOES_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_INTERNACOES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_INTERNACOES):
    case SUCCESS(ACTION_TYPES.UPDATE_INTERNACOES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_INTERNACOES):
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

const apiUrl = 'api/internacoes';
const apiSearchUrl = 'api/_search/internacoes';

// Actions

export const getSearchEntities: ICrudSearchAction<IInternacoes> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_INTERNACOES,
  payload: axios.get<IInternacoes>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IInternacoes> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_INTERNACOES_LIST,
    payload: axios.get<IInternacoes>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IInternacoes> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INTERNACOES,
    payload: axios.get<IInternacoes>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IInternacoes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INTERNACOES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IInternacoes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INTERNACOES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInternacoes> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INTERNACOES,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
