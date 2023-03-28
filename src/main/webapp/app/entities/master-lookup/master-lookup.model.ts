import dayjs from 'dayjs/esm';

export interface IMasterLookup {
  id: number;
  name?: string | null;
  value?: string | null;
  valueTwo?: string | null;
  description?: string | null;
  type?: string | null;
  status?: string | null;
  companyId?: number | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
}

export type NewMasterLookup = Omit<IMasterLookup, 'id'> & { id: null };
