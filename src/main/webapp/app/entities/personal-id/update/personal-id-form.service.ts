import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPersonalId, NewPersonalId } from '../personal-id.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPersonalId for edit and NewPersonalIdFormGroupInput for create.
 */
type PersonalIdFormGroupInput = IPersonalId | PartialWithRequiredKeyOf<NewPersonalId>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPersonalId | NewPersonalId> = Omit<T, 'issueDate' | 'expDate' | 'lastModified'> & {
  issueDate?: string | null;
  expDate?: string | null;
  lastModified?: string | null;
};

type PersonalIdFormRawValue = FormValueOf<IPersonalId>;

type NewPersonalIdFormRawValue = FormValueOf<NewPersonalId>;

type PersonalIdFormDefaults = Pick<NewPersonalId, 'id' | 'issueDate' | 'expDate' | 'lastModified'>;

type PersonalIdFormGroupContent = {
  id: FormControl<PersonalIdFormRawValue['id'] | NewPersonalId['id']>;
  type: FormControl<PersonalIdFormRawValue['type']>;
  number: FormControl<PersonalIdFormRawValue['number']>;
  issueDate: FormControl<PersonalIdFormRawValue['issueDate']>;
  expDate: FormControl<PersonalIdFormRawValue['expDate']>;
  issuingAuthority: FormControl<PersonalIdFormRawValue['issuingAuthority']>;
  docUrl: FormControl<PersonalIdFormRawValue['docUrl']>;
  employeeId: FormControl<PersonalIdFormRawValue['employeeId']>;
  status: FormControl<PersonalIdFormRawValue['status']>;
  companyId: FormControl<PersonalIdFormRawValue['companyId']>;
  lastModified: FormControl<PersonalIdFormRawValue['lastModified']>;
  lastModifiedBy: FormControl<PersonalIdFormRawValue['lastModifiedBy']>;
};

export type PersonalIdFormGroup = FormGroup<PersonalIdFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PersonalIdFormService {
  createPersonalIdFormGroup(personalId: PersonalIdFormGroupInput = { id: null }): PersonalIdFormGroup {
    const personalIdRawValue = this.convertPersonalIdToPersonalIdRawValue({
      ...this.getFormDefaults(),
      ...personalId,
    });
    return new FormGroup<PersonalIdFormGroupContent>({
      id: new FormControl(
        { value: personalIdRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      type: new FormControl(personalIdRawValue.type),
      number: new FormControl(personalIdRawValue.number),
      issueDate: new FormControl(personalIdRawValue.issueDate),
      expDate: new FormControl(personalIdRawValue.expDate),
      issuingAuthority: new FormControl(personalIdRawValue.issuingAuthority),
      docUrl: new FormControl(personalIdRawValue.docUrl),
      employeeId: new FormControl(personalIdRawValue.employeeId),
      status: new FormControl(personalIdRawValue.status),
      companyId: new FormControl(personalIdRawValue.companyId),
      lastModified: new FormControl(personalIdRawValue.lastModified),
      lastModifiedBy: new FormControl(personalIdRawValue.lastModifiedBy),
    });
  }

  getPersonalId(form: PersonalIdFormGroup): IPersonalId | NewPersonalId {
    return this.convertPersonalIdRawValueToPersonalId(form.getRawValue() as PersonalIdFormRawValue | NewPersonalIdFormRawValue);
  }

  resetForm(form: PersonalIdFormGroup, personalId: PersonalIdFormGroupInput): void {
    const personalIdRawValue = this.convertPersonalIdToPersonalIdRawValue({ ...this.getFormDefaults(), ...personalId });
    form.reset(
      {
        ...personalIdRawValue,
        id: { value: personalIdRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PersonalIdFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      issueDate: currentTime,
      expDate: currentTime,
      lastModified: currentTime,
    };
  }

  private convertPersonalIdRawValueToPersonalId(
    rawPersonalId: PersonalIdFormRawValue | NewPersonalIdFormRawValue
  ): IPersonalId | NewPersonalId {
    return {
      ...rawPersonalId,
      issueDate: dayjs(rawPersonalId.issueDate, DATE_TIME_FORMAT),
      expDate: dayjs(rawPersonalId.expDate, DATE_TIME_FORMAT),
      lastModified: dayjs(rawPersonalId.lastModified, DATE_TIME_FORMAT),
    };
  }

  private convertPersonalIdToPersonalIdRawValue(
    personalId: IPersonalId | (Partial<NewPersonalId> & PersonalIdFormDefaults)
  ): PersonalIdFormRawValue | PartialWithRequiredKeyOf<NewPersonalIdFormRawValue> {
    return {
      ...personalId,
      issueDate: personalId.issueDate ? personalId.issueDate.format(DATE_TIME_FORMAT) : undefined,
      expDate: personalId.expDate ? personalId.expDate.format(DATE_TIME_FORMAT) : undefined,
      lastModified: personalId.lastModified ? personalId.lastModified.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
