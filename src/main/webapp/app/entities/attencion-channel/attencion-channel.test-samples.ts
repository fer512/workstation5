import { AttencionChannelType } from 'app/entities/enumerations/attencion-channel-type.model';

import { IAttencionChannel, NewAttencionChannel } from './attencion-channel.model';

export const sampleWithRequiredData: IAttencionChannel = {
  id: 98959,
  name: 'models Grenada Web',
  type: 'VIRTUAL',
};

export const sampleWithPartialData: IAttencionChannel = {
  id: 52279,
  name: 'Consultant Mountain',
  type: 'VIRTUAL',
};

export const sampleWithFullData: IAttencionChannel = {
  id: 63528,
  name: 'secured',
  type: 'PRESENTIAL',
};

export const sampleWithNewData: NewAttencionChannel = {
  name: 'grey quantify male',
  type: 'PRESENTIAL',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
