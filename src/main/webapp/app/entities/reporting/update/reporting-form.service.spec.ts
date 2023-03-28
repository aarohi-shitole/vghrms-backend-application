import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../reporting.test-samples';

import { ReportingFormService } from './reporting-form.service';

describe('Reporting Form Service', () => {
  let service: ReportingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportingFormService);
  });

  describe('Service methods', () => {
    describe('createReportingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeId: expect.any(Object),
            reportingEmpId: expect.any(Object),
            reportingId: expect.any(Object),
            status: expect.any(Object),
            companyId: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });

      it('passing IReporting should create a new form with FormGroup', () => {
        const formGroup = service.createReportingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeId: expect.any(Object),
            reportingEmpId: expect.any(Object),
            reportingId: expect.any(Object),
            status: expect.any(Object),
            companyId: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getReporting', () => {
      it('should return NewReporting for default Reporting initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createReportingFormGroup(sampleWithNewData);

        const reporting = service.getReporting(formGroup) as any;

        expect(reporting).toMatchObject(sampleWithNewData);
      });

      it('should return NewReporting for empty Reporting initial value', () => {
        const formGroup = service.createReportingFormGroup();

        const reporting = service.getReporting(formGroup) as any;

        expect(reporting).toMatchObject({});
      });

      it('should return IReporting', () => {
        const formGroup = service.createReportingFormGroup(sampleWithRequiredData);

        const reporting = service.getReporting(formGroup) as any;

        expect(reporting).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReporting should not enable id FormControl', () => {
        const formGroup = service.createReportingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReporting should disable id FormControl', () => {
        const formGroup = service.createReportingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
