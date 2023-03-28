import dayjs from 'dayjs/esm';

import { IReporting, NewReporting } from './reporting.model';

export const sampleWithRequiredData: IReporting = {
  id: 67489,
};

export const sampleWithPartialData: IReporting = {
  id: 42179,
  reportingId: 11036,
  companyId: 38710,
  lastModifiedBy: 'optical',
};

export const sampleWithFullData: IReporting = {
  id: 22670,
  employeeId: 29957,
  reportingEmpId: 29665,
  reportingId: 27334,
  status: 'wireless Pants application',
  companyId: 68484,
  lastModified: dayjs('2023-03-27T21:23'),
  lastModifiedBy: 'Group synergy withdrawal',
};

export const sampleWithNewData: NewReporting = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
