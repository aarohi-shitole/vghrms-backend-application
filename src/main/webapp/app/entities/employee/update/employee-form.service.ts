import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmployee, NewEmployee } from '../employee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployee for edit and NewEmployeeFormGroupInput for create.
 */
type EmployeeFormGroupInput = IEmployee | PartialWithRequiredKeyOf<NewEmployee>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmployee | NewEmployee> = Omit<T, 'joindate' | 'lastModified'> & {
  joindate?: string | null;
  lastModified?: string | null;
};

type EmployeeFormRawValue = FormValueOf<IEmployee>;

type NewEmployeeFormRawValue = FormValueOf<NewEmployee>;

type EmployeeFormDefaults = Pick<NewEmployee, 'id' | 'joindate' | 'lastModified'>;

type EmployeeFormGroupContent = {
  id: FormControl<EmployeeFormRawValue['id'] | NewEmployee['id']>;
  firstName: FormControl<EmployeeFormRawValue['firstName']>;
  middleName: FormControl<EmployeeFormRawValue['middleName']>;
  lastName: FormControl<EmployeeFormRawValue['lastName']>;
  gender: FormControl<EmployeeFormRawValue['gender']>;
  empUniqueId: FormControl<EmployeeFormRawValue['empUniqueId']>;
  joindate: FormControl<EmployeeFormRawValue['joindate']>;
  status: FormControl<EmployeeFormRawValue['status']>;
  employmentTypeId: FormControl<EmployeeFormRawValue['employmentTypeId']>;
  companyId: FormControl<EmployeeFormRawValue['companyId']>;
  lastModified: FormControl<EmployeeFormRawValue['lastModified']>;
  lastModifiedBy: FormControl<EmployeeFormRawValue['lastModifiedBy']>;
  designation: FormControl<EmployeeFormRawValue['designation']>;
  department: FormControl<EmployeeFormRawValue['department']>;
  branch: FormControl<EmployeeFormRawValue['branch']>;
  region: FormControl<EmployeeFormRawValue['region']>;
};

export type EmployeeFormGroup = FormGroup<EmployeeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeFormService {
  createEmployeeFormGroup(employee: EmployeeFormGroupInput = { id: null }): EmployeeFormGroup {
    const employeeRawValue = this.convertEmployeeToEmployeeRawValue({
      ...this.getFormDefaults(),
      ...employee,
    });
    return new FormGroup<EmployeeFormGroupContent>({
      id: new FormControl(
        { value: employeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(employeeRawValue.firstName),
      middleName: new FormControl(employeeRawValue.middleName),
      lastName: new FormControl(employeeRawValue.lastName),
      gender: new FormControl(employeeRawValue.gender),
      empUniqueId: new FormControl(employeeRawValue.empUniqueId, {
        validators: [Validators.required],
      }),
      joindate: new FormControl(employeeRawValue.joindate),
      status: new FormControl(employeeRawValue.status),
      employmentTypeId: new FormControl(employeeRawValue.employmentTypeId),
      companyId: new FormControl(employeeRawValue.companyId),
      lastModified: new FormControl(employeeRawValue.lastModified),
      lastModifiedBy: new FormControl(employeeRawValue.lastModifiedBy),
      designation: new FormControl(employeeRawValue.designation),
      department: new FormControl(employeeRawValue.department),
      branch: new FormControl(employeeRawValue.branch),
      region: new FormControl(employeeRawValue.region),
    });
  }

  getEmployee(form: EmployeeFormGroup): IEmployee | NewEmployee {
    return this.convertEmployeeRawValueToEmployee(form.getRawValue() as EmployeeFormRawValue | NewEmployeeFormRawValue);
  }

  resetForm(form: EmployeeFormGroup, employee: EmployeeFormGroupInput): void {
    const employeeRawValue = this.convertEmployeeToEmployeeRawValue({ ...this.getFormDefaults(), ...employee });
    form.reset(
      {
        ...employeeRawValue,
        id: { value: employeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      joindate: currentTime,
      lastModified: currentTime,
    };
  }

  private convertEmployeeRawValueToEmployee(rawEmployee: EmployeeFormRawValue | NewEmployeeFormRawValue): IEmployee | NewEmployee {
    return {
      ...rawEmployee,
      joindate: dayjs(rawEmployee.joindate, DATE_TIME_FORMAT),
      lastModified: dayjs(rawEmployee.lastModified, DATE_TIME_FORMAT),
    };
  }

  private convertEmployeeToEmployeeRawValue(
    employee: IEmployee | (Partial<NewEmployee> & EmployeeFormDefaults)
  ): EmployeeFormRawValue | PartialWithRequiredKeyOf<NewEmployeeFormRawValue> {
    return {
      ...employee,
      joindate: employee.joindate ? employee.joindate.format(DATE_TIME_FORMAT) : undefined,
      lastModified: employee.lastModified ? employee.lastModified.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
