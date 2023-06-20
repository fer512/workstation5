import { WaitingRoomAttencionChannelType } from 'app/entities/enumerations/waiting-room-attencion-channel-type.model';

import { IWaitingRoomAttencionChannel, NewWaitingRoomAttencionChannel } from './waiting-room-attencion-channel.model';

export const sampleWithRequiredData: IWaitingRoomAttencionChannel = {
  id: 90363,
  name: 'North Steel Southeast',
  type: 'PRESENTIAL',
};

export const sampleWithPartialData: IWaitingRoomAttencionChannel = {
  id: 5237,
  name: 'Montana Rican Technician',
  type: 'PRESENTIAL',
};

export const sampleWithFullData: IWaitingRoomAttencionChannel = {
  id: 24271,
  name: 'male Awesome',
  type: 'VIRTUAL',
};

export const sampleWithNewData: NewWaitingRoomAttencionChannel = {
  name: 'Jaguar Colorado',
  type: 'VIRTUAL',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
