import dayjs from 'dayjs/esm';

import { IMasterLookup, NewMasterLookup } from './master-lookup.model';

export const sampleWithRequiredData: IMasterLookup = {
  id: 63271,
};

export const sampleWithPartialData: IMasterLookup = {
  id: 30535,
  value: 'Lead niches',
  valueTwo: 'collaboration edge port',
  description: 'communities Intelligent homogeneous',
  companyId: 24095,
  lastModifiedBy: 'Wooden purple withdrawal',
};

export const sampleWithFullData: IMasterLookup = {
  id: 84033,
  name: 'Buckinghamshire Handmade',
  value: 'empower silver Practical',
  valueTwo: 'hour bypassing',
  description: 'transitional Borders Future-proofed',
  type: 'Cloned Frozen',
  status: 'Intelligent',
  companyId: 31322,
  lastModified: dayjs('2023-03-27T16:32'),
  lastModifiedBy: 'Fantastic Walk',
};

export const sampleWithNewData: NewMasterLookup = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
