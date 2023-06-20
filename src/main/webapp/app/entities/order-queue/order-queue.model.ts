import { IQueue } from 'app/entities/queue/queue.model';
import { IWorkerProfile } from 'app/entities/worker-profile/worker-profile.model';

export interface IOrderQueue {
  id: number;
  order?: number | null;
  queue?: Pick<IQueue, 'id'> | null;
  workerProfile?: Pick<IWorkerProfile, 'id'> | null;
}

export type NewOrderQueue = Omit<IOrderQueue, 'id'> & { id: null };
