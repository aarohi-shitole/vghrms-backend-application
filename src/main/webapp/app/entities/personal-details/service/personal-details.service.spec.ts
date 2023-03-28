import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPersonalDetails } from '../personal-details.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../personal-details.test-samples';

import { PersonalDetailsService, RestPersonalDetails } from './personal-details.service';

const requireRestSample: RestPersonalDetails = {
  ...sampleWithRequiredData,
  dateOfBirth: sampleWithRequiredData.dateOfBirth?.format(DATE_FORMAT),
  lastModified: sampleWithRequiredData.lastModified?.toJSON(),
};

describe('PersonalDetails Service', () => {
  let service: PersonalDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IPersonalDetails | IPersonalDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PersonalDetailsService);
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

    it('should create a PersonalDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const personalDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(personalDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PersonalDetails', () => {
      const personalDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(personalDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PersonalDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PersonalDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PersonalDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPersonalDetailsToCollectionIfMissing', () => {
      it('should add a PersonalDetails to an empty array', () => {
        const personalDetails: IPersonalDetails = sampleWithRequiredData;
        expectedResult = service.addPersonalDetailsToCollectionIfMissing([], personalDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personalDetails);
      });

      it('should not add a PersonalDetails to an array that contains it', () => {
        const personalDetails: IPersonalDetails = sampleWithRequiredData;
        const personalDetailsCollection: IPersonalDetails[] = [
          {
            ...personalDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPersonalDetailsToCollectionIfMissing(personalDetailsCollection, personalDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PersonalDetails to an array that doesn't contain it", () => {
        const personalDetails: IPersonalDetails = sampleWithRequiredData;
        const personalDetailsCollection: IPersonalDetails[] = [sampleWithPartialData];
        expectedResult = service.addPersonalDetailsToCollectionIfMissing(personalDetailsCollection, personalDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personalDetails);
      });

      it('should add only unique PersonalDetails to an array', () => {
        const personalDetailsArray: IPersonalDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const personalDetailsCollection: IPersonalDetails[] = [sampleWithRequiredData];
        expectedResult = service.addPersonalDetailsToCollectionIfMissing(personalDetailsCollection, ...personalDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const personalDetails: IPersonalDetails = sampleWithRequiredData;
        const personalDetails2: IPersonalDetails = sampleWithPartialData;
        expectedResult = service.addPersonalDetailsToCollectionIfMissing([], personalDetails, personalDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personalDetails);
        expect(expectedResult).toContain(personalDetails2);
      });

      it('should accept null and undefined values', () => {
        const personalDetails: IPersonalDetails = sampleWithRequiredData;
        expectedResult = service.addPersonalDetailsToCollectionIfMissing([], null, personalDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personalDetails);
      });

      it('should return initial array if no PersonalDetails is added', () => {
        const personalDetailsCollection: IPersonalDetails[] = [sampleWithRequiredData];
        expectedResult = service.addPersonalDetailsToCollectionIfMissing(personalDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(personalDetailsCollection);
      });
    });

    describe('comparePersonalDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePersonalDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePersonalDetails(entity1, entity2);
        const compareResult2 = service.comparePersonalDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePersonalDetails(entity1, entity2);
        const compareResult2 = service.comparePersonalDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePersonalDetails(entity1, entity2);
        const compareResult2 = service.comparePersonalDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
