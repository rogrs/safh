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

import { ILeitos, defaultValue } from 'app/shared/model/leitos.model';

export const ACTION_TYPES = {
  SEARCH_LEITOS: 'leitos/SEARCH_LEITOS',
  FETCH_LEITOS_LIST: 'leitos/FETCH_LEITOS_LIST',
  FETCH_LEITOS: 'leitos/FETCH_LEITOS',
  CREATE_LEITOS: 'leitos/CREATE_LEITOS',
  UPDATE_LEITOS: 'leitos/UPDATE_LEITOS',
  DELETE_LEITOS: 'leitos/DELETE_LEITOS',
  RESET: 'leitos/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILeitos>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type LeitosState = Readonly<typeof initialState>;

// Reducer

export default (state: LeitosState = initialState, action): LeitosState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_LEITOS):
    case REQUEST(ACTION_TYPES.FETCH_LEITOS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LEITOS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LEITOS):
    case REQUEST(ACTION_TYPES.UPDATE_LEITOS):
    case REQUEST(ACTION_TYPES.DELETE_LEITOS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_LEITOS):
    case FAILURE(ACTION_TYPES.FETCH_LEITOS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LEITOS):
    case FAILURE(ACTION_TYPES.CREATE_LEITOS):
    case FAILURE(ACTION_TYPES.UPDATE_LEITOS):
    case FAILURE(ACTION_TYPES.DELETE_LEITOS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_LEITOS):
    case SUCCESS(ACTION_TYPES.FETCH_LEITOS_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_LEITOS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LEITOS):
    case SUCCESS(ACTION_TYPES.UPDATE_LEITOS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LEITOS):
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

const apiUrl = 'api/leitos';
const apiSearchUrl = 'api/_search/leitos';

// Actions

export const getSearchEntities: ICrudSearchAction<ILeitos> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_LEITOS,
  payload: axios.get<ILeitos>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<ILeitos> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_LEITOS_LIST,
    payload: axios.get<ILeitos>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ILeitos> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LEITOS,
    payload: axios.get<ILeitos>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILeitos> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LEITOS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<ILeitos> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LEITOS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILeitos> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LEITOS,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
