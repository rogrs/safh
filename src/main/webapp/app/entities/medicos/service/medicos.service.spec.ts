import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Estados } from 'app/entities/enumerations/estados.model';
import { IMedicos, Medicos } from '../medicos.model';

import { MedicosService } from './medicos.service';

describe('Service Tests', () => {
  describe('Medicos Service', () => {
    let service: MedicosService;
    let httpMock: HttpTestingController;
    let elemDefault: IMedicos;
    let expectedResult: IMedicos | IMedicos[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MedicosService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        crm: 'AAAAAAA',
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

      it('should create a Medicos', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Medicos()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Medicos', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            crm: 'BBBBBB',
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

      it('should partial update a Medicos', () => {
        const patchObject = Object.assign(
          {
            nome: 'BBBBBB',
            cep: 'BBBBBB',
            logradouro: 'BBBBBB',
            bairro: 'BBBBBB',
            uF: 'BBBBBB',
          },
          new Medicos()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Medicos', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            crm: 'BBBBBB',
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

      it('should delete a Medicos', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMedicosToCollectionIfMissing', () => {
        it('should add a Medicos to an empty array', () => {
          const medicos: IMedicos = { id: 123 };
          expectedResult = service.addMedicosToCollectionIfMissing([], medicos);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(medicos);
        });

        it('should not add a Medicos to an array that contains it', () => {
          const medicos: IMedicos = { id: 123 };
          const medicosCollection: IMedicos[] = [
            {
              ...medicos,
            },
            { id: 456 },
          ];
          expectedResult = service.addMedicosToCollectionIfMissing(medicosCollection, medicos);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Medicos to an array that doesn't contain it", () => {
          const medicos: IMedicos = { id: 123 };
          const medicosCollection: IMedicos[] = [{ id: 456 }];
          expectedResult = service.addMedicosToCollectionIfMissing(medicosCollection, medicos);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(medicos);
        });

        it('should add only unique Medicos to an array', () => {
          const medicosArray: IMedicos[] = [{ id: 123 }, { id: 456 }, { id: 6292 }];
          const medicosCollection: IMedicos[] = [{ id: 123 }];
          expectedResult = service.addMedicosToCollectionIfMissing(medicosCollection, ...medicosArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const medicos: IMedicos = { id: 123 };
          const medicos2: IMedicos = { id: 456 };
          expectedResult = service.addMedicosToCollectionIfMissing([], medicos, medicos2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(medicos);
          expect(expectedResult).toContain(medicos2);
        });

        it('should accept null and undefined values', () => {
          const medicos: IMedicos = { id: 123 };
          expectedResult = service.addMedicosToCollectionIfMissing([], null, medicos, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(medicos);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
