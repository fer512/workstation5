import { WorkerProfileAttencionChannelType } from 'app/entities/enumerations/worker-profile-attencion-channel-type.model';

import { IWorkerProfileAttencionChannel, NewWorkerProfileAttencionChannel } from './worker-profile-attencion-channel.model';

export const sampleWithRequiredData: IWorkerProfileAttencionChannel = {
  id: 20213,
  name: 'limply pink mop',
  type: 'VIRTUAL',
};

export const sampleWithPartialData: IWorkerProfileAttencionChannel = {
  id: 61012,
  name: 'whenever',
  type: 'PRESENTIAL',
};

export const sampleWithFullData: IWorkerProfileAttencionChannel = {
  id: 82669,
  name: 'seamless Southwest',
  type: 'PRESENTIAL',
};

export const sampleWithNewData: NewWorkerProfileAttencionChannel = {
  name: 'modi deposit Junctions',
  type: 'VIRTUAL',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
