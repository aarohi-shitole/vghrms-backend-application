import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../family-info.test-samples';

import { FamilyInfoFormService } from './family-info-form.service';

describe('FamilyInfo Form Service', () => {
  let service: FamilyInfoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FamilyInfoFormService);
  });

  describe('Service methods', () => {
    describe('createFamilyInfoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFamilyInfoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            dateOfBirth: expect.any(Object),
            relation: expect.any(Object),
            addressId: expect.any(Object),
            isEmployed: expect.any(Object),
            employedAt: expect.any(Object),
            employeeId: expect.any(Object),
            status: expect.any(Object),
            companyId: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });

      it('passing IFamilyInfo should create a new form with FormGroup', () => {
        const formGroup = service.createFamilyInfoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            dateOfBirth: expect.any(Object),
            relation: expect.any(Object),
            addressId: expect.any(Object),
            isEmployed: expect.any(Object),
            employedAt: expect.any(Object),
            employeeId: expect.any(Object),
            status: expect.any(Object),
            companyId: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getFamilyInfo', () => {
      it('should return NewFamilyInfo for default FamilyInfo initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFamilyInfoFormGroup(sampleWithNewData);

        const familyInfo = service.getFamilyInfo(formGroup) as any;

        expect(familyInfo).toMatchObject(sampleWithNewData);
      });

      it('should return NewFamilyInfo for empty FamilyInfo initial value', () => {
        const formGroup = service.createFamilyInfoFormGroup();

        const familyInfo = service.getFamilyInfo(formGroup) as any;

        expect(familyInfo).toMatchObject({});
      });

      it('should return IFamilyInfo', () => {
        const formGroup = service.createFamilyInfoFormGroup(sampleWithRequiredData);

        const familyInfo = service.getFamilyInfo(formGroup) as any;

        expect(familyInfo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFamilyInfo should not enable id FormControl', () => {
        const formGroup = service.createFamilyInfoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFamilyInfo should disable id FormControl', () => {
        const formGroup = service.createFamilyInfoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
