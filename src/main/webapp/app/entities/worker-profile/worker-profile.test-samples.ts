import { WorkerProfileStatus } from 'app/entities/enumerations/worker-profile-status.model';

import { IWorkerProfile, NewWorkerProfile } from './worker-profile.model';

export const sampleWithRequiredData: IWorkerProfile = {
  id: 61605,
  name: 'Cab noisily archive',
  status: 'DISABLED',
};

export const sampleWithPartialData: IWorkerProfile = {
  id: 15537,
  name: 'hacking Ohio Passenger',
  status: 'ACTIVE',
};

export const sampleWithFullData: IWorkerProfile = {
  id: 13770,
  name: 'tesla',
  status: 'ACTIVE',
};

export const sampleWithNewData: NewWorkerProfile = {
  name: 'RAM synergies migrate',
  status: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
