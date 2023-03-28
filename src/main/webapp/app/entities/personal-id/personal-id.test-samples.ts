import dayjs from 'dayjs/esm';

import { IPersonalId, NewPersonalId } from './personal-id.model';

export const sampleWithRequiredData: IPersonalId = {
  id: 87944,
};

export const sampleWithPartialData: IPersonalId = {
  id: 62066,
  number: 'invoice',
  issueDate: dayjs('2023-03-27T05:37'),
  expDate: dayjs('2023-03-27T17:50'),
  issuingAuthority: 'Fresh e-services',
  docUrl: 'Markets sensor',
  employeeId: 61246,
  status: 'port back-end',
  companyId: 14505,
  lastModifiedBy: 'maroon Research',
};

export const sampleWithFullData: IPersonalId = {
  id: 34640,
  type: 'Games Books Personal',
  number: 'SMTP Architect',
  issueDate: dayjs('2023-03-27T13:48'),
  expDate: dayjs('2023-03-27T19:29'),
  issuingAuthority: 'circuit calculate',
  docUrl: 'calculating composite Plastic',
  employeeId: 74498,
  status: 'Botswana Mouse Pennsylvania',
  companyId: 98871,
  lastModified: dayjs('2023-03-27T13:33'),
  lastModifiedBy: 'holistic',
};

export const sampleWithNewData: NewPersonalId = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
