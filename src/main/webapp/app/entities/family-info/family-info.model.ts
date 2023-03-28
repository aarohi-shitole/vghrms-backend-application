import dayjs from 'dayjs/esm';

export interface IFamilyInfo {
  id: number;
  name?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  relation?: string | null;
  addressId?: number | null;
  isEmployed?: boolean | null;
  employedAt?: string | null;
  employeeId?: number | null;
  status?: string | null;
  companyId?: number | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
}

export type NewFamilyInfo = Omit<IFamilyInfo, 'id'> & { id: null };
