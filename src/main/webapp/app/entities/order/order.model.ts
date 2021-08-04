import * as dayjs from 'dayjs';
import { IClient } from 'app/entities/client/client.model';

export interface IOrder {
  id?: string;
  symbol?: string | null;
  price?: number | null;
  quantity?: number | null;
  createAt?: dayjs.Dayjs | null;
  modifiedAt?: dayjs.Dayjs | null;
  completed?: boolean | null;
  failed?: boolean | null;
  client?: IClient | null;
}

export class Order implements IOrder {
  constructor(
    public id?: string,
    public symbol?: string | null,
    public price?: number | null,
    public quantity?: number | null,
    public createAt?: dayjs.Dayjs | null,
    public modifiedAt?: dayjs.Dayjs | null,
    public completed?: boolean | null,
    public failed?: boolean | null,
    public client?: IClient | null
  ) {
    this.completed = this.completed ?? false;
    this.failed = this.failed ?? false;
  }
}

export function getOrderIdentifier(order: IOrder): string | undefined {
  return order.id;
}
