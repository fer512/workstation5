import { WaitingRoomStatus } from 'app/entities/enumerations/waiting-room-status.model';

import { IWaitingRoom, NewWaitingRoom } from './waiting-room.model';

export const sampleWithRequiredData: IWaitingRoom = {
  id: 39730,
  name: 'quantifying',
  status: 'DISABLED',
};

export const sampleWithPartialData: IWaitingRoom = {
  id: 41462,
  name: 'though',
  status: 'ACTIVE',
};

export const sampleWithFullData: IWaitingRoom = {
  id: 58454,
  name: 'Practical',
  status: 'ACTIVE',
};

export const sampleWithNewData: NewWaitingRoom = {
  name: 'Northeast',
  status: 'DISABLED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
