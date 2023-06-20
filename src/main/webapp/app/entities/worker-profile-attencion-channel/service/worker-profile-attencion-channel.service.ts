import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkerProfileAttencionChannel, NewWorkerProfileAttencionChannel } from '../worker-profile-attencion-channel.model';

export type PartialUpdateWorkerProfileAttencionChannel = Partial<IWorkerProfileAttencionChannel> &
  Pick<IWorkerProfileAttencionChannel, 'id'>;

export type EntityResponseType = HttpResponse<IWorkerProfileAttencionChannel>;
export type EntityArrayResponseType = HttpResponse<IWorkerProfileAttencionChannel[]>;

@Injectable({ providedIn: 'root' })
export class WorkerProfileAttencionChannelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/worker-profile-attencion-channels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(workerProfileAttencionChannel: NewWorkerProfileAttencionChannel): Observable<EntityResponseType> {
    return this.http.post<IWorkerProfileAttencionChannel>(this.resourceUrl, workerProfileAttencionChannel, { observe: 'response' });
  }

  update(workerProfileAttencionChannel: IWorkerProfileAttencionChannel): Observable<EntityResponseType> {
    return this.http.put<IWorkerProfileAttencionChannel>(
      `${this.resourceUrl}/${this.getWorkerProfileAttencionChannelIdentifier(workerProfileAttencionChannel)}`,
      workerProfileAttencionChannel,
      { observe: 'response' }
    );
  }

  partialUpdate(workerProfileAttencionChannel: PartialUpdateWorkerProfileAttencionChannel): Observable<EntityResponseType> {
    return this.http.patch<IWorkerProfileAttencionChannel>(
      `${this.resourceUrl}/${this.getWorkerProfileAttencionChannelIdentifier(workerProfileAttencionChannel)}`,
      workerProfileAttencionChannel,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkerProfileAttencionChannel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkerProfileAttencionChannel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWorkerProfileAttencionChannelIdentifier(workerProfileAttencionChannel: Pick<IWorkerProfileAttencionChannel, 'id'>): number {
    return workerProfileAttencionChannel.id;
  }

  compareWorkerProfileAttencionChannel(
    o1: Pick<IWorkerProfileAttencionChannel, 'id'> | null,
    o2: Pick<IWorkerProfileAttencionChannel, 'id'> | null
  ): boolean {
    return o1 && o2
      ? this.getWorkerProfileAttencionChannelIdentifier(o1) === this.getWorkerProfileAttencionChannelIdentifier(o2)
      : o1 === o2;
  }

  addWorkerProfileAttencionChannelToCollectionIfMissing<Type extends Pick<IWorkerProfileAttencionChannel, 'id'>>(
    workerProfileAttencionChannelCollection: Type[],
    ...workerProfileAttencionChannelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workerProfileAttencionChannels: Type[] = workerProfileAttencionChannelsToCheck.filter(isPresent);
    if (workerProfileAttencionChannels.length > 0) {
      const workerProfileAttencionChannelCollectionIdentifiers = workerProfileAttencionChannelCollection.map(
        workerProfileAttencionChannelItem => this.getWorkerProfileAttencionChannelIdentifier(workerProfileAttencionChannelItem)!
      );
      const workerProfileAttencionChannelsToAdd = workerProfileAttencionChannels.filter(workerProfileAttencionChannelItem => {
        const workerProfileAttencionChannelIdentifier = this.getWorkerProfileAttencionChannelIdentifier(workerProfileAttencionChannelItem);
        if (workerProfileAttencionChannelCollectionIdentifiers.includes(workerProfileAttencionChannelIdentifier)) {
          return false;
        }
        workerProfileAttencionChannelCollectionIdentifiers.push(workerProfileAttencionChannelIdentifier);
        return true;
      });
      return [...workerProfileAttencionChannelsToAdd, ...workerProfileAttencionChannelCollection];
    }
    return workerProfileAttencionChannelCollection;
  }
}
