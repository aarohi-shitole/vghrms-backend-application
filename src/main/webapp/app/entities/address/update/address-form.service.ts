import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAddress, NewAddress } from '../address.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAddress for edit and NewAddressFormGroupInput for create.
 */
type AddressFormGroupInput = IAddress | PartialWithRequiredKeyOf<NewAddress>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAddress | NewAddress> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

type AddressFormRawValue = FormValueOf<IAddress>;

type NewAddressFormRawValue = FormValueOf<NewAddress>;

type AddressFormDefaults = Pick<NewAddress, 'id' | 'defaultAdd' | 'lastModified'>;

type AddressFormGroupContent = {
  id: FormControl<AddressFormRawValue['id'] | NewAddress['id']>;
  type: FormControl<AddressFormRawValue['type']>;
  line1: FormControl<AddressFormRawValue['line1']>;
  line2: FormControl<AddressFormRawValue['line2']>;
  country: FormControl<AddressFormRawValue['country']>;
  state: FormControl<AddressFormRawValue['state']>;
  city: FormControl<AddressFormRawValue['city']>;
  pincode: FormControl<AddressFormRawValue['pincode']>;
  defaultAdd: FormControl<AddressFormRawValue['defaultAdd']>;
  landMark: FormControl<AddressFormRawValue['landMark']>;
  longitude: FormControl<AddressFormRawValue['longitude']>;
  latitude: FormControl<AddressFormRawValue['latitude']>;
  refTable: FormControl<AddressFormRawValue['refTable']>;
  refTableId: FormControl<AddressFormRawValue['refTableId']>;
  status: FormControl<AddressFormRawValue['status']>;
  companyId: FormControl<AddressFormRawValue['companyId']>;
  lastModified: FormControl<AddressFormRawValue['lastModified']>;
  lastModifiedBy: FormControl<AddressFormRawValue['lastModifiedBy']>;
};

export type AddressFormGroup = FormGroup<AddressFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AddressFormService {
  createAddressFormGroup(address: AddressFormGroupInput = { id: null }): AddressFormGroup {
    const addressRawValue = this.convertAddressToAddressRawValue({
      ...this.getFormDefaults(),
      ...address,
    });
    return new FormGroup<AddressFormGroupContent>({
      id: new FormControl(
        { value: addressRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      type: new FormControl(addressRawValue.type),
      line1: new FormControl(addressRawValue.line1),
      line2: new FormControl(addressRawValue.line2),
      country: new FormControl(addressRawValue.country),
      state: new FormControl(addressRawValue.state),
      city: new FormControl(addressRawValue.city),
      pincode: new FormControl(addressRawValue.pincode),
      defaultAdd: new FormControl(addressRawValue.defaultAdd),
      landMark: new FormControl(addressRawValue.landMark),
      longitude: new FormControl(addressRawValue.longitude),
      latitude: new FormControl(addressRawValue.latitude),
      refTable: new FormControl(addressRawValue.refTable),
      refTableId: new FormControl(addressRawValue.refTableId),
      status: new FormControl(addressRawValue.status),
      companyId: new FormControl(addressRawValue.companyId),
      lastModified: new FormControl(addressRawValue.lastModified),
      lastModifiedBy: new FormControl(addressRawValue.lastModifiedBy),
    });
  }

  getAddress(form: AddressFormGroup): IAddress | NewAddress {
    return this.convertAddressRawValueToAddress(form.getRawValue() as AddressFormRawValue | NewAddressFormRawValue);
  }

  resetForm(form: AddressFormGroup, address: AddressFormGroupInput): void {
    const addressRawValue = this.convertAddressToAddressRawValue({ ...this.getFormDefaults(), ...address });
    form.reset(
      {
        ...addressRawValue,
        id: { value: addressRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AddressFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      defaultAdd: false,
      lastModified: currentTime,
    };
  }

  private convertAddressRawValueToAddress(rawAddress: AddressFormRawValue | NewAddressFormRawValue): IAddress | NewAddress {
    return {
      ...rawAddress,
      lastModified: dayjs(rawAddress.lastModified, DATE_TIME_FORMAT),
    };
  }

  private convertAddressToAddressRawValue(
    address: IAddress | (Partial<NewAddress> & AddressFormDefaults)
  ): AddressFormRawValue | PartialWithRequiredKeyOf<NewAddressFormRawValue> {
    return {
      ...address,
      lastModified: address.lastModified ? address.lastModified.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
