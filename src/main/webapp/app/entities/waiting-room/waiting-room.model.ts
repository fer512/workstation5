import { IWaitingRoomAttencionChannel } from 'app/entities/waiting-room-attencion-channel/waiting-room-attencion-channel.model';
import { ICompany } from 'app/entities/company/company.model';
import { IBranch } from 'app/entities/branch/branch.model';
import { WaitingRoomStatus } from 'app/entities/enumerations/waiting-room-status.model';

export interface IWaitingRoom {
  id: number;
  name?: string | null;
  status?: keyof typeof WaitingRoomStatus | null;
  attencionChannel?: Pick<IWaitingRoomAttencionChannel, 'id'> | null;
  company?: Pick<ICompany, 'id'> | null;
  branches?: Pick<IBranch, 'id'>[] | null;
}

export type NewWaitingRoom = Omit<IWaitingRoom, 'id'> & { id: null };
