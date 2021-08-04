import * as dayjs from 'dayjs';
import { IClient } from 'app/entities/client/client.model';

export interface IExchange {
  id?: string;
  name?: string | null;
  apikey?: string | null;
  secrit?: string | null;
  activated?: boolean | null;
  createAt?: dayjs.Dayjs | null;
  modifiedAt?: dayjs.Dayjs | null;
  client?: IClient | null;
}

export class Exchange implements IExchange {
  constructor(
    public id?: string,
    public name?: string | null,
    public apikey?: string | null,
    public secrit?: string | null,
    public activated?: boolean | null,
    public createAt?: dayjs.Dayjs | null,
    public modifiedAt?: dayjs.Dayjs | null,
    public client?: IClient | null
  ) {
    this.activated = this.activated ?? false;
  }
}

export function getExchangeIdentifier(exchange: IExchange): string | undefined {
  return exchange.id;
}
