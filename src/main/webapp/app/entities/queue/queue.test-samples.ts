import { QueueStatus } from 'app/entities/enumerations/queue-status.model';

import { IQueue, NewQueue } from './queue.model';

export const sampleWithRequiredData: IQueue = {
  id: 44547,
  name: 'Hybrid Blues management',
  status: 'DISABLED',
};

export const sampleWithPartialData: IQueue = {
  id: 44985,
  name: 'Synergized',
  status: 'DISABLED',
};

export const sampleWithFullData: IQueue = {
  id: 52237,
  name: 'Hong',
  status: 'ACTIVE',
};

export const sampleWithNewData: NewQueue = {
  name: 'Adventure',
  status: 'DISABLED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
