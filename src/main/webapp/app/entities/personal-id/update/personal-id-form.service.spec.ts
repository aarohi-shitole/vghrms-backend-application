import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../personal-id.test-samples';

import { PersonalIdFormService } from './personal-id-form.service';

describe('PersonalId Form Service', () => {
  let service: PersonalIdFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PersonalIdFormService);
  });

  describe('Service methods', () => {
    describe('createPersonalIdFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPersonalIdFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            number: expect.any(Object),
            issueDate: expect.any(Object),
            expDate: expect.any(Object),
            issuingAuthority: expect.any(Object),
            docUrl: expect.any(Object),
            employeeId: expect.any(Object),
            status: expect.any(Object),
            companyId: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });

      it('passing IPersonalId should create a new form with FormGroup', () => {
        const formGroup = service.createPersonalIdFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            number: expect.any(Object),
            issueDate: expect.any(Object),
            expDate: expect.any(Object),
            issuingAuthority: expect.any(Object),
            docUrl: expect.any(Object),
            employeeId: expect.any(Object),
            status: expect.any(Object),
            companyId: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getPersonalId', () => {
      it('should return NewPersonalId for default PersonalId initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPersonalIdFormGroup(sampleWithNewData);

        const personalId = service.getPersonalId(formGroup) as any;

        expect(personalId).toMatchObject(sampleWithNewData);
      });

      it('should return NewPersonalId for empty PersonalId initial value', () => {
        const formGroup = service.createPersonalIdFormGroup();

        const personalId = service.getPersonalId(formGroup) as any;

        expect(personalId).toMatchObject({});
      });

      it('should return IPersonalId', () => {
        const formGroup = service.createPersonalIdFormGroup(sampleWithRequiredData);

        const personalId = service.getPersonalId(formGroup) as any;

        expect(personalId).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPersonalId should not enable id FormControl', () => {
        const formGroup = service.createPersonalIdFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPersonalId should disable id FormControl', () => {
        const formGroup = service.createPersonalIdFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
