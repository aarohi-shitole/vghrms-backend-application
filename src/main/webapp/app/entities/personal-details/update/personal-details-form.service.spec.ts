import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../personal-details.test-samples';

import { PersonalDetailsFormService } from './personal-details-form.service';

describe('PersonalDetails Form Service', () => {
  let service: PersonalDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PersonalDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createPersonalDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPersonalDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            telephoneNo: expect.any(Object),
            nationality: expect.any(Object),
            maritalStatus: expect.any(Object),
            religion: expect.any(Object),
            employeeId: expect.any(Object),
            bloodGroup: expect.any(Object),
            dateOfBirth: expect.any(Object),
            status: expect.any(Object),
            companyId: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });

      it('passing IPersonalDetails should create a new form with FormGroup', () => {
        const formGroup = service.createPersonalDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            telephoneNo: expect.any(Object),
            nationality: expect.any(Object),
            maritalStatus: expect.any(Object),
            religion: expect.any(Object),
            employeeId: expect.any(Object),
            bloodGroup: expect.any(Object),
            dateOfBirth: expect.any(Object),
            status: expect.any(Object),
            companyId: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getPersonalDetails', () => {
      it('should return NewPersonalDetails for default PersonalDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPersonalDetailsFormGroup(sampleWithNewData);

        const personalDetails = service.getPersonalDetails(formGroup) as any;

        expect(personalDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewPersonalDetails for empty PersonalDetails initial value', () => {
        const formGroup = service.createPersonalDetailsFormGroup();

        const personalDetails = service.getPersonalDetails(formGroup) as any;

        expect(personalDetails).toMatchObject({});
      });

      it('should return IPersonalDetails', () => {
        const formGroup = service.createPersonalDetailsFormGroup(sampleWithRequiredData);

        const personalDetails = service.getPersonalDetails(formGroup) as any;

        expect(personalDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPersonalDetails should not enable id FormControl', () => {
        const formGroup = service.createPersonalDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPersonalDetails should disable id FormControl', () => {
        const formGroup = service.createPersonalDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
