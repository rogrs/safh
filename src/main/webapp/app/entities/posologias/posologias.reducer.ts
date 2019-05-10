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

import { IPosologias, defaultValue } from 'app/shared/model/posologias.model';

export const ACTION_TYPES = {
  SEARCH_POSOLOGIAS: 'posologias/SEARCH_POSOLOGIAS',
  FETCH_POSOLOGIAS_LIST: 'posologias/FETCH_POSOLOGIAS_LIST',
  FETCH_POSOLOGIAS: 'posologias/FETCH_POSOLOGIAS',
  CREATE_POSOLOGIAS: 'posologias/CREATE_POSOLOGIAS',
  UPDATE_POSOLOGIAS: 'posologias/UPDATE_POSOLOGIAS',
  DELETE_POSOLOGIAS: 'posologias/DELETE_POSOLOGIAS',
  RESET: 'posologias/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPosologias>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PosologiasState = Readonly<typeof initialState>;

// Reducer

export default (state: PosologiasState = initialState, action): PosologiasState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_POSOLOGIAS):
    case REQUEST(ACTION_TYPES.FETCH_POSOLOGIAS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_POSOLOGIAS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_POSOLOGIAS):
    case REQUEST(ACTION_TYPES.UPDATE_POSOLOGIAS):
    case REQUEST(ACTION_TYPES.DELETE_POSOLOGIAS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_POSOLOGIAS):
    case FAILURE(ACTION_TYPES.FETCH_POSOLOGIAS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_POSOLOGIAS):
    case FAILURE(ACTION_TYPES.CREATE_POSOLOGIAS):
    case FAILURE(ACTION_TYPES.UPDATE_POSOLOGIAS):
    case FAILURE(ACTION_TYPES.DELETE_POSOLOGIAS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_POSOLOGIAS):
    case SUCCESS(ACTION_TYPES.FETCH_POSOLOGIAS_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_POSOLOGIAS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_POSOLOGIAS):
    case SUCCESS(ACTION_TYPES.UPDATE_POSOLOGIAS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_POSOLOGIAS):
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

const apiUrl = 'api/posologias';
const apiSearchUrl = 'api/_search/posologias';

// Actions

export const getSearchEntities: ICrudSearchAction<IPosologias> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_POSOLOGIAS,
  payload: axios.get<IPosologias>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IPosologias> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_POSOLOGIAS_LIST,
    payload: axios.get<IPosologias>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPosologias> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_POSOLOGIAS,
    payload: axios.get<IPosologias>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPosologias> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_POSOLOGIAS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IPosologias> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_POSOLOGIAS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPosologias> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_POSOLOGIAS,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
