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

import { IFabricantes, defaultValue } from 'app/shared/model/fabricantes.model';

export const ACTION_TYPES = {
  SEARCH_FABRICANTES: 'fabricantes/SEARCH_FABRICANTES',
  FETCH_FABRICANTES_LIST: 'fabricantes/FETCH_FABRICANTES_LIST',
  FETCH_FABRICANTES: 'fabricantes/FETCH_FABRICANTES',
  CREATE_FABRICANTES: 'fabricantes/CREATE_FABRICANTES',
  UPDATE_FABRICANTES: 'fabricantes/UPDATE_FABRICANTES',
  DELETE_FABRICANTES: 'fabricantes/DELETE_FABRICANTES',
  RESET: 'fabricantes/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFabricantes>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type FabricantesState = Readonly<typeof initialState>;

// Reducer

export default (state: FabricantesState = initialState, action): FabricantesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_FABRICANTES):
    case REQUEST(ACTION_TYPES.FETCH_FABRICANTES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FABRICANTES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FABRICANTES):
    case REQUEST(ACTION_TYPES.UPDATE_FABRICANTES):
    case REQUEST(ACTION_TYPES.DELETE_FABRICANTES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_FABRICANTES):
    case FAILURE(ACTION_TYPES.FETCH_FABRICANTES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FABRICANTES):
    case FAILURE(ACTION_TYPES.CREATE_FABRICANTES):
    case FAILURE(ACTION_TYPES.UPDATE_FABRICANTES):
    case FAILURE(ACTION_TYPES.DELETE_FABRICANTES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_FABRICANTES):
    case SUCCESS(ACTION_TYPES.FETCH_FABRICANTES_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_FABRICANTES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FABRICANTES):
    case SUCCESS(ACTION_TYPES.UPDATE_FABRICANTES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FABRICANTES):
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

const apiUrl = 'api/fabricantes';
const apiSearchUrl = 'api/_search/fabricantes';

// Actions

export const getSearchEntities: ICrudSearchAction<IFabricantes> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_FABRICANTES,
  payload: axios.get<IFabricantes>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IFabricantes> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FABRICANTES_LIST,
    payload: axios.get<IFabricantes>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IFabricantes> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FABRICANTES,
    payload: axios.get<IFabricantes>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFabricantes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FABRICANTES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IFabricantes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FABRICANTES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFabricantes> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FABRICANTES,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
