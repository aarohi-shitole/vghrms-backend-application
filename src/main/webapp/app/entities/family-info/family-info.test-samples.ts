import dayjs from 'dayjs/esm';

import { IFamilyInfo, NewFamilyInfo } from './family-info.model';

export const sampleWithRequiredData: IFamilyInfo = {
  id: 35605,
};

export const sampleWithPartialData: IFamilyInfo = {
  id: 22997,
  employeeId: 62934,
  status: 'array',
  companyId: 5228,
  lastModifiedBy: 'defect Gorgeous uniform',
};

export const sampleWithFullData: IFamilyInfo = {
  id: 37105,
  name: 'Isle',
  dateOfBirth: dayjs('2023-03-27'),
  relation: 'Synergized navigating',
  addressId: 53795,
  isEmployed: false,
  employedAt: 'transmit',
  employeeId: 2316,
  status: 'hacking De-engineered',
  companyId: 61297,
  lastModified: dayjs('2023-03-28T02:21'),
  lastModifiedBy: 'circuit deliverables parsing',
};

export const sampleWithNewData: NewFamilyInfo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
