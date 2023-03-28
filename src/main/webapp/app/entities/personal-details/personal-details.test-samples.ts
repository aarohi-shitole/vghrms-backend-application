import dayjs from 'dayjs/esm';

import { IPersonalDetails, NewPersonalDetails } from './personal-details.model';

export const sampleWithRequiredData: IPersonalDetails = {
  id: 97979,
};

export const sampleWithPartialData: IPersonalDetails = {
  id: 72500,
  nationality: 'Incredible Cliff programming',
  employeeId: 99542,
  bloodGroup: 'Plastic',
  dateOfBirth: dayjs('2023-03-28'),
  status: 'Coves Sausages',
  companyId: 2071,
  lastModified: dayjs('2023-03-27T13:09'),
  lastModifiedBy: 'input leverage',
};

export const sampleWithFullData: IPersonalDetails = {
  id: 6200,
  telephoneNo: 'haptic',
  nationality: 'Bike Avon',
  maritalStatus: 'Rwanda open-source Fresh',
  religion: 'policy Island withdrawal',
  employeeId: 12155,
  bloodGroup: 'Barbados Pennsylvania',
  dateOfBirth: dayjs('2023-03-27'),
  status: 'Computers',
  companyId: 16237,
  lastModified: dayjs('2023-03-27T16:49'),
  lastModifiedBy: 'Jamaican Computers',
};

export const sampleWithNewData: NewPersonalDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
