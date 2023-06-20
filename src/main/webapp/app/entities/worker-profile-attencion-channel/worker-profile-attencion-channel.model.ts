import { WorkerProfileAttencionChannelType } from 'app/entities/enumerations/worker-profile-attencion-channel-type.model';

export interface IWorkerProfileAttencionChannel {
  id: number;
  name?: string | null;
  type?: keyof typeof WorkerProfileAttencionChannelType | null;
}

export type NewWorkerProfileAttencionChannel = Omit<IWorkerProfileAttencionChannel, 'id'> & { id: null };
