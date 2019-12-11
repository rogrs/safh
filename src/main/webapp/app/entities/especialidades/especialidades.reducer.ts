import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEspecialidades, defaultValue } from 'app/shared/model/especialidades.model';

export const ACTION_TYPES = {
  SEARCH_ESPECIALIDADES: 'especialidades/SEARCH_ESPECIALIDADES',
  FETCH_ESPECIALIDADES_LIST: 'especialidades/FETCH_ESPECIALIDADES_LIST',
  FETCH_ESPECIALIDADES: 'especialidades/FETCH_ESPECIALIDADES',
  CREATE_ESPECIALIDADES: 'especialidades/CREATE_ESPECIALIDADES',
  UPDATE_ESPECIALIDADES: 'especialidades/UPDATE_ESPECIALIDADES',
  DELETE_ESPECIALIDADES: 'especialidades/DELETE_ESPECIALIDADES',
  RESET: 'especialidades/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEspecialidades>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type EspecialidadesState = Readonly<typeof initialState>;

// Reducer

export default (state: EspecialidadesState = initialState, action): EspecialidadesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ESPECIALIDADES):
    case REQUEST(ACTION_TYPES.FETCH_ESPECIALIDADES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ESPECIALIDADES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ESPECIALIDADES):
    case REQUEST(ACTION_TYPES.UPDATE_ESPECIALIDADES):
    case REQUEST(ACTION_TYPES.DELETE_ESPECIALIDADES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ESPECIALIDADES):
    case FAILURE(ACTION_TYPES.FETCH_ESPECIALIDADES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ESPECIALIDADES):
    case FAILURE(ACTION_TYPES.CREATE_ESPECIALIDADES):
    case FAILURE(ACTION_TYPES.UPDATE_ESPECIALIDADES):
    case FAILURE(ACTION_TYPES.DELETE_ESPECIALIDADES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ESPECIALIDADES):
    case SUCCESS(ACTION_TYPES.FETCH_ESPECIALIDADES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ESPECIALIDADES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ESPECIALIDADES):
    case SUCCESS(ACTION_TYPES.UPDATE_ESPECIALIDADES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ESPECIALIDADES):
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

const apiUrl = 'api/especialidades';
const apiSearchUrl = 'api/_search/especialidades';

// Actions

export const getSearchEntities: ICrudSearchAction<IEspecialidades> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ESPECIALIDADES,
  payload: axios.get<IEspecialidades>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IEspecialidades> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ESPECIALIDADES_LIST,
  payload: axios.get<IEspecialidades>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IEspecialidades> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ESPECIALIDADES,
    payload: axios.get<IEspecialidades>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEspecialidades> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ESPECIALIDADES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEspecialidades> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ESPECIALIDADES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEspecialidades> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ESPECIALIDADES,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
