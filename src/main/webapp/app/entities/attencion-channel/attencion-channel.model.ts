import { AttencionChannelType } from 'app/entities/enumerations/attencion-channel-type.model';

export interface IAttencionChannel {
  id: number;
  name?: string | null;
  type?: keyof typeof AttencionChannelType | null;
}

export type NewAttencionChannel = Omit<IAttencionChannel, 'id'> & { id: null };
