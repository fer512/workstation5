import { IAttencionChannel } from 'app/entities/attencion-channel/attencion-channel.model';
import { ICompany } from 'app/entities/company/company.model';
import { IBranch } from 'app/entities/branch/branch.model';
import { QueueStatus } from 'app/entities/enumerations/queue-status.model';

export interface IQueue {
  id: number;
  name?: string | null;
  status?: keyof typeof QueueStatus | null;
  attencionChannel?: Pick<IAttencionChannel, 'id'> | null;
  company?: Pick<ICompany, 'id'> | null;
  branches?: Pick<IBranch, 'id'>[] | null;
}

export type NewQueue = Omit<IQueue, 'id'> & { id: null };
