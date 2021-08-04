import * as dayjs from 'dayjs';
import { IClient } from 'app/entities/client/client.model';

export interface ITracker {
  id?: string;
  activated?: boolean | null;
  createAt?: dayjs.Dayjs | null;
  modifiedAt?: dayjs.Dayjs | null;
  activationBegin?: dayjs.Dayjs | null;
  activationEnd?: dayjs.Dayjs | null;
  client?: IClient | null;
}

export class Tracker implements ITracker {
  constructor(
    public id?: string,
    public activated?: boolean | null,
    public createAt?: dayjs.Dayjs | null,
    public modifiedAt?: dayjs.Dayjs | null,
    public activationBegin?: dayjs.Dayjs | null,
    public activationEnd?: dayjs.Dayjs | null,
    public client?: IClient | null
  ) {
    this.activated = this.activated ?? false;
  }
}

export function getTrackerIdentifier(tracker: ITracker): string | undefined {
  return tracker.id;
}
