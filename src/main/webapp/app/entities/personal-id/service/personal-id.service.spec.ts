import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonalId } from '../personal-id.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../personal-id.test-samples';

import { PersonalIdService, RestPersonalId } from './personal-id.service';

const requireRestSample: RestPersonalId = {
  ...sampleWithRequiredData,
  issueDate: sampleWithRequiredData.issueDate?.toJSON(),
  expDate: sampleWithRequiredData.expDate?.toJSON(),
  lastModified: sampleWithRequiredData.lastModified?.toJSON(),
};

describe('PersonalId Service', () => {
  let service: PersonalIdService;
  let httpMock: HttpTestingController;
  let expectedResult: IPersonalId | IPersonalId[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PersonalIdService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PersonalId', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const personalId = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(personalId).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PersonalId', () => {
      const personalId = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(personalId).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PersonalId', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PersonalId', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PersonalId', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPersonalIdToCollectionIfMissing', () => {
      it('should add a PersonalId to an empty array', () => {
        const personalId: IPersonalId = sampleWithRequiredData;
        expectedResult = service.addPersonalIdToCollectionIfMissing([], personalId);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personalId);
      });

      it('should not add a PersonalId to an array that contains it', () => {
        const personalId: IPersonalId = sampleWithRequiredData;
        const personalIdCollection: IPersonalId[] = [
          {
            ...personalId,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPersonalIdToCollectionIfMissing(personalIdCollection, personalId);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PersonalId to an array that doesn't contain it", () => {
        const personalId: IPersonalId = sampleWithRequiredData;
        const personalIdCollection: IPersonalId[] = [sampleWithPartialData];
        expectedResult = service.addPersonalIdToCollectionIfMissing(personalIdCollection, personalId);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personalId);
      });

      it('should add only unique PersonalId to an array', () => {
        const personalIdArray: IPersonalId[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const personalIdCollection: IPersonalId[] = [sampleWithRequiredData];
        expectedResult = service.addPersonalIdToCollectionIfMissing(personalIdCollection, ...personalIdArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const personalId: IPersonalId = sampleWithRequiredData;
        const personalId2: IPersonalId = sampleWithPartialData;
        expectedResult = service.addPersonalIdToCollectionIfMissing([], personalId, personalId2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personalId);
        expect(expectedResult).toContain(personalId2);
      });

      it('should accept null and undefined values', () => {
        const personalId: IPersonalId = sampleWithRequiredData;
        expectedResult = service.addPersonalIdToCollectionIfMissing([], null, personalId, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personalId);
      });

      it('should return initial array if no PersonalId is added', () => {
        const personalIdCollection: IPersonalId[] = [sampleWithRequiredData];
        expectedResult = service.addPersonalIdToCollectionIfMissing(personalIdCollection, undefined, null);
        expect(expectedResult).toEqual(personalIdCollection);
      });
    });

    describe('comparePersonalId', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePersonalId(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePersonalId(entity1, entity2);
        const compareResult2 = service.comparePersonalId(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePersonalId(entity1, entity2);
        const compareResult2 = service.comparePersonalId(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePersonalId(entity1, entity2);
        const compareResult2 = service.comparePersonalId(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
