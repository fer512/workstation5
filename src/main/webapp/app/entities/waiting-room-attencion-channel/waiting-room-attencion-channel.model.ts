import { WaitingRoomAttencionChannelType } from 'app/entities/enumerations/waiting-room-attencion-channel-type.model';

export interface IWaitingRoomAttencionChannel {
  id: number;
  name?: string | null;
  type?: keyof typeof WaitingRoomAttencionChannelType | null;
}

export type NewWaitingRoomAttencionChannel = Omit<IWaitingRoomAttencionChannel, 'id'> & { id: null };
