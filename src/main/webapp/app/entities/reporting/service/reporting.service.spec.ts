import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReporting } from '../reporting.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../reporting.test-samples';

import { ReportingService, RestReporting } from './reporting.service';

const requireRestSample: RestReporting = {
  ...sampleWithRequiredData,
  lastModified: sampleWithRequiredData.lastModified?.toJSON(),
};

describe('Reporting Service', () => {
  let service: ReportingService;
  let httpMock: HttpTestingController;
  let expectedResult: IReporting | IReporting[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportingService);
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

    it('should create a Reporting', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const reporting = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reporting).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Reporting', () => {
      const reporting = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reporting).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Reporting', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Reporting', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Reporting', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReportingToCollectionIfMissing', () => {
      it('should add a Reporting to an empty array', () => {
        const reporting: IReporting = sampleWithRequiredData;
        expectedResult = service.addReportingToCollectionIfMissing([], reporting);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reporting);
      });

      it('should not add a Reporting to an array that contains it', () => {
        const reporting: IReporting = sampleWithRequiredData;
        const reportingCollection: IReporting[] = [
          {
            ...reporting,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportingToCollectionIfMissing(reportingCollection, reporting);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Reporting to an array that doesn't contain it", () => {
        const reporting: IReporting = sampleWithRequiredData;
        const reportingCollection: IReporting[] = [sampleWithPartialData];
        expectedResult = service.addReportingToCollectionIfMissing(reportingCollection, reporting);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reporting);
      });

      it('should add only unique Reporting to an array', () => {
        const reportingArray: IReporting[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportingCollection: IReporting[] = [sampleWithRequiredData];
        expectedResult = service.addReportingToCollectionIfMissing(reportingCollection, ...reportingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reporting: IReporting = sampleWithRequiredData;
        const reporting2: IReporting = sampleWithPartialData;
        expectedResult = service.addReportingToCollectionIfMissing([], reporting, reporting2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reporting);
        expect(expectedResult).toContain(reporting2);
      });

      it('should accept null and undefined values', () => {
        const reporting: IReporting = sampleWithRequiredData;
        expectedResult = service.addReportingToCollectionIfMissing([], null, reporting, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reporting);
      });

      it('should return initial array if no Reporting is added', () => {
        const reportingCollection: IReporting[] = [sampleWithRequiredData];
        expectedResult = service.addReportingToCollectionIfMissing(reportingCollection, undefined, null);
        expect(expectedResult).toEqual(reportingCollection);
      });
    });

    describe('compareReporting', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReporting(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareReporting(entity1, entity2);
        const compareResult2 = service.compareReporting(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareReporting(entity1, entity2);
        const compareResult2 = service.compareReporting(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareReporting(entity1, entity2);
        const compareResult2 = service.compareReporting(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
