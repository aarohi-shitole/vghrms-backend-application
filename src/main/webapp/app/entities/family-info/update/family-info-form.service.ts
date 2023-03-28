import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFamilyInfo, NewFamilyInfo } from '../family-info.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFamilyInfo for edit and NewFamilyInfoFormGroupInput for create.
 */
type FamilyInfoFormGroupInput = IFamilyInfo | PartialWithRequiredKeyOf<NewFamilyInfo>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFamilyInfo | NewFamilyInfo> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

type FamilyInfoFormRawValue = FormValueOf<IFamilyInfo>;

type NewFamilyInfoFormRawValue = FormValueOf<NewFamilyInfo>;

type FamilyInfoFormDefaults = Pick<NewFamilyInfo, 'id' | 'isEmployed' | 'lastModified'>;

type FamilyInfoFormGroupContent = {
  id: FormControl<FamilyInfoFormRawValue['id'] | NewFamilyInfo['id']>;
  name: FormControl<FamilyInfoFormRawValue['name']>;
  dateOfBirth: FormControl<FamilyInfoFormRawValue['dateOfBirth']>;
  relation: FormControl<FamilyInfoFormRawValue['relation']>;
  addressId: FormControl<FamilyInfoFormRawValue['addressId']>;
  isEmployed: FormControl<FamilyInfoFormRawValue['isEmployed']>;
  employedAt: FormControl<FamilyInfoFormRawValue['employedAt']>;
  employeeId: FormControl<FamilyInfoFormRawValue['employeeId']>;
  status: FormControl<FamilyInfoFormRawValue['status']>;
  companyId: FormControl<FamilyInfoFormRawValue['companyId']>;
  lastModified: FormControl<FamilyInfoFormRawValue['lastModified']>;
  lastModifiedBy: FormControl<FamilyInfoFormRawValue['lastModifiedBy']>;
};

export type FamilyInfoFormGroup = FormGroup<FamilyInfoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FamilyInfoFormService {
  createFamilyInfoFormGroup(familyInfo: FamilyInfoFormGroupInput = { id: null }): FamilyInfoFormGroup {
    const familyInfoRawValue = this.convertFamilyInfoToFamilyInfoRawValue({
      ...this.getFormDefaults(),
      ...familyInfo,
    });
    return new FormGroup<FamilyInfoFormGroupContent>({
      id: new FormControl(
        { value: familyInfoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(familyInfoRawValue.name),
      dateOfBirth: new FormControl(familyInfoRawValue.dateOfBirth),
      relation: new FormControl(familyInfoRawValue.relation),
      addressId: new FormControl(familyInfoRawValue.addressId),
      isEmployed: new FormControl(familyInfoRawValue.isEmployed),
      employedAt: new FormControl(familyInfoRawValue.employedAt),
      employeeId: new FormControl(familyInfoRawValue.employeeId),
      status: new FormControl(familyInfoRawValue.status),
      companyId: new FormControl(familyInfoRawValue.companyId),
      lastModified: new FormControl(familyInfoRawValue.lastModified),
      lastModifiedBy: new FormControl(familyInfoRawValue.lastModifiedBy),
    });
  }

  getFamilyInfo(form: FamilyInfoFormGroup): IFamilyInfo | NewFamilyInfo {
    return this.convertFamilyInfoRawValueToFamilyInfo(form.getRawValue() as FamilyInfoFormRawValue | NewFamilyInfoFormRawValue);
  }

  resetForm(form: FamilyInfoFormGroup, familyInfo: FamilyInfoFormGroupInput): void {
    const familyInfoRawValue = this.convertFamilyInfoToFamilyInfoRawValue({ ...this.getFormDefaults(), ...familyInfo });
    form.reset(
      {
        ...familyInfoRawValue,
        id: { value: familyInfoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FamilyInfoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isEmployed: false,
      lastModified: currentTime,
    };
  }

  private convertFamilyInfoRawValueToFamilyInfo(
    rawFamilyInfo: FamilyInfoFormRawValue | NewFamilyInfoFormRawValue
  ): IFamilyInfo | NewFamilyInfo {
    return {
      ...rawFamilyInfo,
      lastModified: dayjs(rawFamilyInfo.lastModified, DATE_TIME_FORMAT),
    };
  }

  private convertFamilyInfoToFamilyInfoRawValue(
    familyInfo: IFamilyInfo | (Partial<NewFamilyInfo> & FamilyInfoFormDefaults)
  ): FamilyInfoFormRawValue | PartialWithRequiredKeyOf<NewFamilyInfoFormRawValue> {
    return {
      ...familyInfo,
      lastModified: familyInfo.lastModified ? familyInfo.lastModified.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
