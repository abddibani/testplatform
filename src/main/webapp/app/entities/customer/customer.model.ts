import * as dayjs from 'dayjs';

export interface ICustomer {
  id?: string;
  email?: string | null;
  lastname?: string | null;
  firstname?: string | null;
  phone?: string | null;
  createAt?: dayjs.Dayjs | null;
  modifiedAt?: dayjs.Dayjs | null;
}

export class Customer implements ICustomer {
  constructor(
    public id?: string,
    public email?: string | null,
    public lastname?: string | null,
    public firstname?: string | null,
    public phone?: string | null,
    public createAt?: dayjs.Dayjs | null,
    public modifiedAt?: dayjs.Dayjs | null
  ) {}
}

export function getCustomerIdentifier(customer: ICustomer): string | undefined {
  return customer.id;
}
