import { ICompany } from 'app/entities/company/company.model';
import { IBranch } from 'app/entities/branch/branch.model';
import { IWaitingRoom } from 'app/entities/waiting-room/waiting-room.model';
import { WorkerStatus } from 'app/entities/enumerations/worker-status.model';

export interface IWorker {
  id: number;
  name?: string | null;
  status?: keyof typeof WorkerStatus | null;
  company?: Pick<ICompany, 'id'> | null;
  branches?: Pick<IBranch, 'id'>[] | null;
  waitingRoom?: Pick<IWaitingRoom, 'id'> | null;
}

export type NewWorker = Omit<IWorker, 'id'> & { id: null };
