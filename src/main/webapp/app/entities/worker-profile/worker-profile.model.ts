import { IWorkerProfileAttencionChannel } from 'app/entities/worker-profile-attencion-channel/worker-profile-attencion-channel.model';
import { ICompany } from 'app/entities/company/company.model';
import { IBranch } from 'app/entities/branch/branch.model';
import { WorkerProfileStatus } from 'app/entities/enumerations/worker-profile-status.model';

export interface IWorkerProfile {
  id: number;
  name?: string | null;
  status?: keyof typeof WorkerProfileStatus | null;
  attencionChannel?: Pick<IWorkerProfileAttencionChannel, 'id'> | null;
  company?: Pick<ICompany, 'id'> | null;
  branches?: Pick<IBranch, 'id'>[] | null;
}

export type NewWorkerProfile = Omit<IWorkerProfile, 'id'> & { id: null };
