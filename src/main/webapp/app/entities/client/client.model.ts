import { IExchange } from 'app/entities/exchange/exchange.model';
import { IOrder } from 'app/entities/order/order.model';
import { ITracker } from 'app/entities/tracker/tracker.model';

export interface IClient {
  id?: string;
  email?: string | null;
  username?: string | null;
  password?: string | null;
  activated?: boolean | null;
  exchanges?: IExchange[] | null;
  orders?: IOrder[] | null;
  trackers?: ITracker[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: string,
    public email?: string | null,
    public username?: string | null,
    public password?: string | null,
    public activated?: boolean | null,
    public exchanges?: IExchange[] | null,
    public orders?: IOrder[] | null,
    public trackers?: ITracker[] | null
  ) {
    this.activated = this.activated ?? false;
  }
}

export function getClientIdentifier(client: IClient): string | undefined {
  return client.id;
}
