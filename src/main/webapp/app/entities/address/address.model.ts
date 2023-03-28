import dayjs from 'dayjs/esm';

export interface IAddress {
  id: number;
  type?: string | null;
  line1?: string | null;
  line2?: string | null;
  country?: string | null;
  state?: string | null;
  city?: string | null;
  pincode?: string | null;
  defaultAdd?: boolean | null;
  landMark?: string | null;
  longitude?: number | null;
  latitude?: number | null;
  refTable?: string | null;
  refTableId?: number | null;
  status?: string | null;
  companyId?: number | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
}

export type NewAddress = Omit<IAddress, 'id'> & { id: null };
