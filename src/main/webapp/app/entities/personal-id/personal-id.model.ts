import dayjs from 'dayjs/esm';

export interface IPersonalId {
  id: number;
  type?: string | null;
  number?: string | null;
  issueDate?: dayjs.Dayjs | null;
  expDate?: dayjs.Dayjs | null;
  issuingAuthority?: string | null;
  docUrl?: string | null;
  employeeId?: number | null;
  status?: string | null;
  companyId?: number | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
}

export type NewPersonalId = Omit<IPersonalId, 'id'> & { id: null };
