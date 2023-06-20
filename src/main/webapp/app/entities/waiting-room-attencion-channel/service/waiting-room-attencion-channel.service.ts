import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWaitingRoomAttencionChannel, NewWaitingRoomAttencionChannel } from '../waiting-room-attencion-channel.model';

export type PartialUpdateWaitingRoomAttencionChannel = Partial<IWaitingRoomAttencionChannel> & Pick<IWaitingRoomAttencionChannel, 'id'>;

export type EntityResponseType = HttpResponse<IWaitingRoomAttencionChannel>;
export type EntityArrayResponseType = HttpResponse<IWaitingRoomAttencionChannel[]>;

@Injectable({ providedIn: 'root' })
export class WaitingRoomAttencionChannelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/waiting-room-attencion-channels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(waitingRoomAttencionChannel: NewWaitingRoomAttencionChannel): Observable<EntityResponseType> {
    return this.http.post<IWaitingRoomAttencionChannel>(this.resourceUrl, waitingRoomAttencionChannel, { observe: 'response' });
  }

  update(waitingRoomAttencionChannel: IWaitingRoomAttencionChannel): Observable<EntityResponseType> {
    return this.http.put<IWaitingRoomAttencionChannel>(
      `${this.resourceUrl}/${this.getWaitingRoomAttencionChannelIdentifier(waitingRoomAttencionChannel)}`,
      waitingRoomAttencionChannel,
      { observe: 'response' }
    );
  }

  partialUpdate(waitingRoomAttencionChannel: PartialUpdateWaitingRoomAttencionChannel): Observable<EntityResponseType> {
    return this.http.patch<IWaitingRoomAttencionChannel>(
      `${this.resourceUrl}/${this.getWaitingRoomAttencionChannelIdentifier(waitingRoomAttencionChannel)}`,
      waitingRoomAttencionChannel,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWaitingRoomAttencionChannel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWaitingRoomAttencionChannel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWaitingRoomAttencionChannelIdentifier(waitingRoomAttencionChannel: Pick<IWaitingRoomAttencionChannel, 'id'>): number {
    return waitingRoomAttencionChannel.id;
  }

  compareWaitingRoomAttencionChannel(
    o1: Pick<IWaitingRoomAttencionChannel, 'id'> | null,
    o2: Pick<IWaitingRoomAttencionChannel, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getWaitingRoomAttencionChannelIdentifier(o1) === this.getWaitingRoomAttencionChannelIdentifier(o2) : o1 === o2;
  }

  addWaitingRoomAttencionChannelToCollectionIfMissing<Type extends Pick<IWaitingRoomAttencionChannel, 'id'>>(
    waitingRoomAttencionChannelCollection: Type[],
    ...waitingRoomAttencionChannelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const waitingRoomAttencionChannels: Type[] = waitingRoomAttencionChannelsToCheck.filter(isPresent);
    if (waitingRoomAttencionChannels.length > 0) {
      const waitingRoomAttencionChannelCollectionIdentifiers = waitingRoomAttencionChannelCollection.map(
        waitingRoomAttencionChannelItem => this.getWaitingRoomAttencionChannelIdentifier(waitingRoomAttencionChannelItem)!
      );
      const waitingRoomAttencionChannelsToAdd = waitingRoomAttencionChannels.filter(waitingRoomAttencionChannelItem => {
        const waitingRoomAttencionChannelIdentifier = this.getWaitingRoomAttencionChannelIdentifier(waitingRoomAttencionChannelItem);
        if (waitingRoomAttencionChannelCollectionIdentifiers.includes(waitingRoomAttencionChannelIdentifier)) {
          return false;
        }
        waitingRoomAttencionChannelCollectionIdentifiers.push(waitingRoomAttencionChannelIdentifier);
        return true;
      });
      return [...waitingRoomAttencionChannelsToAdd, ...waitingRoomAttencionChannelCollection];
    }
    return waitingRoomAttencionChannelCollection;
  }
}
