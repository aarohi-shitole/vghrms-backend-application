import dayjs from 'dayjs/esm';

import { IAddress, NewAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {
  id: 88754,
};

export const sampleWithPartialData: IAddress = {
  id: 34407,
  type: 'efficient Montana',
  line1: 'interfaces Intelligent Ameliorated',
  state: 'open-source Car hybrid',
  city: 'Kingsport',
  pincode: 'Swiss',
  defaultAdd: true,
  longitude: 33699,
  latitude: 36601,
  status: 'Soap synthesizing intranet',
  companyId: 17957,
  lastModified: dayjs('2023-03-27T11:07'),
  lastModifiedBy: 'PNG',
};

export const sampleWithFullData: IAddress = {
  id: 37391,
  type: 'Birr experiences Re-contextualized',
  line1: 'Compatible black maximize',
  line2: 'solution Plaza',
  country: 'Sao Tome and Principe',
  state: 'Investment virtual',
  city: 'Harveyhaven',
  pincode: 'experiences Books',
  defaultAdd: false,
  landMark: 'microchip Territories',
  longitude: 19987,
  latitude: 67691,
  refTable: 'SDD',
  refTableId: 45211,
  status: 'indigo',
  companyId: 1206,
  lastModified: dayjs('2023-03-27T11:43'),
  lastModifiedBy: 'solid',
};

export const sampleWithNewData: NewAddress = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
