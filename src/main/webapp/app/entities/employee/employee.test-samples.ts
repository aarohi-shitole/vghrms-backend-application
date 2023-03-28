import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 7813,
  empUniqueId: 'transmitting',
};

export const sampleWithPartialData: IEmployee = {
  id: 8063,
  firstName: 'Brooke',
  lastName: 'Labadie',
  gender: 'empower e-tailers internet',
  empUniqueId: 'Implementation Pizza',
  joindate: dayjs('2023-03-27T21:47'),
  status: 'View National Analyst',
  companyId: 49970,
};

export const sampleWithFullData: IEmployee = {
  id: 97734,
  firstName: 'Vicenta',
  middleName: 'user-centric invoice',
  lastName: 'Hayes',
  gender: 'Loan challenge Iranian',
  empUniqueId: 'Rubber Forward',
  joindate: dayjs('2023-03-27T18:59'),
  status: 'Island Concrete Music',
  employmentTypeId: 45515,
  companyId: 16199,
  lastModified: dayjs('2023-03-27T07:07'),
  lastModifiedBy: 'Account Lesotho Bhutanese',
};

export const sampleWithNewData: NewEmployee = {
  empUniqueId: 'Tactics',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
