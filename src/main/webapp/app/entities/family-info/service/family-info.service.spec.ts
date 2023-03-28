import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFamilyInfo } from '../family-info.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../family-info.test-samples';

import { FamilyInfoService, RestFamilyInfo } from './family-info.service';

const requireRestSample: RestFamilyInfo = {
  ...sampleWithRequiredData,
  dateOfBirth: sampleWithRequiredData.dateOfBirth?.format(DATE_FORMAT),
  lastModified: sampleWithRequiredData.lastModified?.toJSON(),
};

describe('FamilyInfo Service', () => {
  let service: FamilyInfoService;
  let httpMock: HttpTestingController;
  let expectedResult: IFamilyInfo | IFamilyInfo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FamilyInfoService);
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

    it('should create a FamilyInfo', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const familyInfo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(familyInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FamilyInfo', () => {
      const familyInfo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(familyInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FamilyInfo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FamilyInfo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FamilyInfo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFamilyInfoToCollectionIfMissing', () => {
      it('should add a FamilyInfo to an empty array', () => {
        const familyInfo: IFamilyInfo = sampleWithRequiredData;
        expectedResult = service.addFamilyInfoToCollectionIfMissing([], familyInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(familyInfo);
      });

      it('should not add a FamilyInfo to an array that contains it', () => {
        const familyInfo: IFamilyInfo = sampleWithRequiredData;
        const familyInfoCollection: IFamilyInfo[] = [
          {
            ...familyInfo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFamilyInfoToCollectionIfMissing(familyInfoCollection, familyInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FamilyInfo to an array that doesn't contain it", () => {
        const familyInfo: IFamilyInfo = sampleWithRequiredData;
        const familyInfoCollection: IFamilyInfo[] = [sampleWithPartialData];
        expectedResult = service.addFamilyInfoToCollectionIfMissing(familyInfoCollection, familyInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(familyInfo);
      });

      it('should add only unique FamilyInfo to an array', () => {
        const familyInfoArray: IFamilyInfo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const familyInfoCollection: IFamilyInfo[] = [sampleWithRequiredData];
        expectedResult = service.addFamilyInfoToCollectionIfMissing(familyInfoCollection, ...familyInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const familyInfo: IFamilyInfo = sampleWithRequiredData;
        const familyInfo2: IFamilyInfo = sampleWithPartialData;
        expectedResult = service.addFamilyInfoToCollectionIfMissing([], familyInfo, familyInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(familyInfo);
        expect(expectedResult).toContain(familyInfo2);
      });

      it('should accept null and undefined values', () => {
        const familyInfo: IFamilyInfo = sampleWithRequiredData;
        expectedResult = service.addFamilyInfoToCollectionIfMissing([], null, familyInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(familyInfo);
      });

      it('should return initial array if no FamilyInfo is added', () => {
        const familyInfoCollection: IFamilyInfo[] = [sampleWithRequiredData];
        expectedResult = service.addFamilyInfoToCollectionIfMissing(familyInfoCollection, undefined, null);
        expect(expectedResult).toEqual(familyInfoCollection);
      });
    });

    describe('compareFamilyInfo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFamilyInfo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFamilyInfo(entity1, entity2);
        const compareResult2 = service.compareFamilyInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFamilyInfo(entity1, entity2);
        const compareResult2 = service.compareFamilyInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFamilyInfo(entity1, entity2);
        const compareResult2 = service.compareFamilyInfo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
