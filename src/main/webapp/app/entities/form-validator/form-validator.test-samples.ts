import dayjs from 'dayjs/esm';

import { IFormValidator, NewFormValidator } from './form-validator.model';

export const sampleWithRequiredData: IFormValidator = {
  id: 37814,
};

export const sampleWithPartialData: IFormValidator = {
  id: 95162,
  formName: 'Incredible 24/7 Buckinghamshire',
  fieldName: 'teal',
  lastModified: dayjs('2023-03-28T04:31'),
  lastModifiedBy: 'blue Cheese empower',
};

export const sampleWithFullData: IFormValidator = {
  id: 76177,
  type: 'Paradigm haptic',
  value: 'Fields',
  formName: 'Electronics bypassing drive',
  fieldName: 'e-enable quantifying',
  status: 'application',
  companyId: 42935,
  lastModified: dayjs('2023-03-28T02:55'),
  lastModifiedBy: 'deposit circuit Architect',
};

export const sampleWithNewData: NewFormValidator = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
