import { WorkerStatus } from 'app/entities/enumerations/worker-status.model';

import { IWorker, NewWorker } from './worker.model';

export const sampleWithRequiredData: IWorker = {
  id: 76436,
  name: 'besides',
  status: 'DISABLED',
};

export const sampleWithPartialData: IWorker = {
  id: 41770,
  name: 'silver Bicycle',
  status: 'DISABLED',
};

export const sampleWithFullData: IWorker = {
  id: 10022,
  name: 'oof',
  status: 'DISABLED',
};

export const sampleWithNewData: NewWorker = {
  name: 'Home',
  status: 'DISABLED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
