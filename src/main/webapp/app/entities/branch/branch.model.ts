import { ICompany } from 'app/entities/company/company.model';
import { IWaitingRoom } from 'app/entities/waiting-room/waiting-room.model';
import { IQueue } from 'app/entities/queue/queue.model';
import { IWorker } from 'app/entities/worker/worker.model';
import { IWorkerProfile } from 'app/entities/worker-profile/worker-profile.model';
import { BranchStatus } from 'app/entities/enumerations/branch-status.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IBranch {
  id: number;
  name?: string | null;
  status?: keyof typeof BranchStatus | null;
  language?: keyof typeof Language | null;
  company?: Pick<ICompany, 'id'> | null;
  waitingRooms?: Pick<IWaitingRoom, 'id'>[] | null;
  queues?: Pick<IQueue, 'id'>[] | null;
  workers?: Pick<IWorker, 'id'>[] | null;
  workerProfiles?: Pick<IWorkerProfile, 'id'>[] | null;
}

export type NewBranch = Omit<IBranch, 'id'> & { id: null };
