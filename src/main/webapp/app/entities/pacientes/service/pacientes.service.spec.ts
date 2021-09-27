import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Estados } from 'app/entities/enumerations/estados.model';
import { IPacientes, Pacientes } from '../pacientes.model';

import { PacientesService } from './pacientes.service';

describe('Service Tests', () => {
  describe('Pacientes Service', () => {
    let service: PacientesService;
    let httpMock: HttpTestingController;
    let elemDefault: IPacientes;
    let expectedResult: IPacientes | IPacientes[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PacientesService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        prontuario: 0,
        nome: 'AAAAAAA',
        cpf: 'AAAAAAA',
        email: 'AAAAAAA',
        cep: 'AAAAAAA',
        logradouro: 'AAAAAAA',
        numero: 'AAAAAAA',
        complemento: 'AAAAAAA',
        bairro: 'AAAAAAA',
        cidade: 'AAAAAAA',
        uF: Estados.AC,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Pacientes', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Pacientes()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Pacientes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            prontuario: 1,
            nome: 'BBBBBB',
            cpf: 'BBBBBB',
            email: 'BBBBBB',
            cep: 'BBBBBB',
            logradouro: 'BBBBBB',
            numero: 'BBBBBB',
            complemento: 'BBBBBB',
            bairro: 'BBBBBB',
            cidade: 'BBBBBB',
            uF: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Pacientes', () => {
        const patchObject = Object.assign(
          {
            prontuario: 1,
            nome: 'BBBBBB',
            cep: 'BBBBBB',
            logradouro: 'BBBBBB',
            numero: 'BBBBBB',
            complemento: 'BBBBBB',
            bairro: 'BBBBBB',
            cidade: 'BBBBBB',
          },
          new Pacientes()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Pacientes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            prontuario: 1,
            nome: 'BBBBBB',
            cpf: 'BBBBBB',
            email: 'BBBBBB',
            cep: 'BBBBBB',
            logradouro: 'BBBBBB',
            numero: 'BBBBBB',
            complemento: 'BBBBBB',
            bairro: 'BBBBBB',
            cidade: 'BBBBBB',
            uF: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Pacientes', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPacientesToCollectionIfMissing', () => {
        it('should add a Pacientes to an empty array', () => {
          const pacientes: IPacientes = { id: 123 };
          expectedResult = service.addPacientesToCollectionIfMissing([], pacientes);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pacientes);
        });

        it('should not add a Pacientes to an array that contains it', () => {
          const pacientes: IPacientes = { id: 123 };
          const pacientesCollection: IPacientes[] = [
            {
              ...pacientes,
            },
            { id: 456 },
          ];
          expectedResult = service.addPacientesToCollectionIfMissing(pacientesCollection, pacientes);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Pacientes to an array that doesn't contain it", () => {
          const pacientes: IPacientes = { id: 123 };
          const pacientesCollection: IPacientes[] = [{ id: 456 }];
          expectedResult = service.addPacientesToCollectionIfMissing(pacientesCollection, pacientes);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pacientes);
        });

        it('should add only unique Pacientes to an array', () => {
          const pacientesArray: IPacientes[] = [{ id: 123 }, { id: 456 }, { id: 86203 }];
          const pacientesCollection: IPacientes[] = [{ id: 123 }];
          expectedResult = service.addPacientesToCollectionIfMissing(pacientesCollection, ...pacientesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const pacientes: IPacientes = { id: 123 };
          const pacientes2: IPacientes = { id: 456 };
          expectedResult = service.addPacientesToCollectionIfMissing([], pacientes, pacientes2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pacientes);
          expect(expectedResult).toContain(pacientes2);
        });

        it('should accept null and undefined values', () => {
          const pacientes: IPacientes = { id: 123 };
          expectedResult = service.addPacientesToCollectionIfMissing([], null, pacientes, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pacientes);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
