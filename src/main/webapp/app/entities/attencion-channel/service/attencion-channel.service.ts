import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttencionChannel, NewAttencionChannel } from '../attencion-channel.model';

export type PartialUpdateAttencionChannel = Partial<IAttencionChannel> & Pick<IAttencionChannel, 'id'>;

export type EntityResponseType = HttpResponse<IAttencionChannel>;
export type EntityArrayResponseType = HttpResponse<IAttencionChannel[]>;

@Injectable({ providedIn: 'root' })
export class AttencionChannelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attencion-channels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(attencionChannel: NewAttencionChannel): Observable<EntityResponseType> {
    return this.http.post<IAttencionChannel>(this.resourceUrl, attencionChannel, { observe: 'response' });
  }

  update(attencionChannel: IAttencionChannel): Observable<EntityResponseType> {
    return this.http.put<IAttencionChannel>(
      `${this.resourceUrl}/${this.getAttencionChannelIdentifier(attencionChannel)}`,
      attencionChannel,
      { observe: 'response' }
    );
  }

  partialUpdate(attencionChannel: PartialUpdateAttencionChannel): Observable<EntityResponseType> {
    return this.http.patch<IAttencionChannel>(
      `${this.resourceUrl}/${this.getAttencionChannelIdentifier(attencionChannel)}`,
      attencionChannel,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAttencionChannel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttencionChannel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAttencionChannelIdentifier(attencionChannel: Pick<IAttencionChannel, 'id'>): number {
    return attencionChannel.id;
  }

  compareAttencionChannel(o1: Pick<IAttencionChannel, 'id'> | null, o2: Pick<IAttencionChannel, 'id'> | null): boolean {
    return o1 && o2 ? this.getAttencionChannelIdentifier(o1) === this.getAttencionChannelIdentifier(o2) : o1 === o2;
  }

  addAttencionChannelToCollectionIfMissing<Type extends Pick<IAttencionChannel, 'id'>>(
    attencionChannelCollection: Type[],
    ...attencionChannelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const attencionChannels: Type[] = attencionChannelsToCheck.filter(isPresent);
    if (attencionChannels.length > 0) {
      const attencionChannelCollectionIdentifiers = attencionChannelCollection.map(
        attencionChannelItem => this.getAttencionChannelIdentifier(attencionChannelItem)!
      );
      const attencionChannelsToAdd = attencionChannels.filter(attencionChannelItem => {
        const attencionChannelIdentifier = this.getAttencionChannelIdentifier(attencionChannelItem);
        if (attencionChannelCollectionIdentifiers.includes(attencionChannelIdentifier)) {
          return false;
        }
        attencionChannelCollectionIdentifiers.push(attencionChannelIdentifier);
        return true;
      });
      return [...attencionChannelsToAdd, ...attencionChannelCollection];
    }
    return attencionChannelCollection;
  }
}
