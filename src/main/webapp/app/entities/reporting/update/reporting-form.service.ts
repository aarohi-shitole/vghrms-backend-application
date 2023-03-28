import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReporting, NewReporting } from '../reporting.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReporting for edit and NewReportingFormGroupInput for create.
 */
type ReportingFormGroupInput = IReporting | PartialWithRequiredKeyOf<NewReporting>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReporting | NewReporting> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

type ReportingFormRawValue = FormValueOf<IReporting>;

type NewReportingFormRawValue = FormValueOf<NewReporting>;

type ReportingFormDefaults = Pick<NewReporting, 'id' | 'lastModified'>;

type ReportingFormGroupContent = {
  id: FormControl<ReportingFormRawValue['id'] | NewReporting['id']>;
  employeeId: FormControl<ReportingFormRawValue['employeeId']>;
  reportingEmpId: FormControl<ReportingFormRawValue['reportingEmpId']>;
  reportingId: FormControl<ReportingFormRawValue['reportingId']>;
  status: FormControl<ReportingFormRawValue['status']>;
  companyId: FormControl<ReportingFormRawValue['companyId']>;
  lastModified: FormControl<ReportingFormRawValue['lastModified']>;
  lastModifiedBy: FormControl<ReportingFormRawValue['lastModifiedBy']>;
};

export type ReportingFormGroup = FormGroup<ReportingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportingFormService {
  createReportingFormGroup(reporting: ReportingFormGroupInput = { id: null }): ReportingFormGroup {
    const reportingRawValue = this.convertReportingToReportingRawValue({
      ...this.getFormDefaults(),
      ...reporting,
    });
    return new FormGroup<ReportingFormGroupContent>({
      id: new FormControl(
        { value: reportingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      employeeId: new FormControl(reportingRawValue.employeeId),
      reportingEmpId: new FormControl(reportingRawValue.reportingEmpId),
      reportingId: new FormControl(reportingRawValue.reportingId),
      status: new FormControl(reportingRawValue.status),
      companyId: new FormControl(reportingRawValue.companyId),
      lastModified: new FormControl(reportingRawValue.lastModified),
      lastModifiedBy: new FormControl(reportingRawValue.lastModifiedBy),
    });
  }

  getReporting(form: ReportingFormGroup): IReporting | NewReporting {
    return this.convertReportingRawValueToReporting(form.getRawValue() as ReportingFormRawValue | NewReportingFormRawValue);
  }

  resetForm(form: ReportingFormGroup, reporting: ReportingFormGroupInput): void {
    const reportingRawValue = this.convertReportingToReportingRawValue({ ...this.getFormDefaults(), ...reporting });
    form.reset(
      {
        ...reportingRawValue,
        id: { value: reportingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ReportingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastModified: currentTime,
    };
  }

  private convertReportingRawValueToReporting(rawReporting: ReportingFormRawValue | NewReportingFormRawValue): IReporting | NewReporting {
    return {
      ...rawReporting,
      lastModified: dayjs(rawReporting.lastModified, DATE_TIME_FORMAT),
    };
  }

  private convertReportingToReportingRawValue(
    reporting: IReporting | (Partial<NewReporting> & ReportingFormDefaults)
  ): ReportingFormRawValue | PartialWithRequiredKeyOf<NewReportingFormRawValue> {
    return {
      ...reporting,
      lastModified: reporting.lastModified ? reporting.lastModified.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
