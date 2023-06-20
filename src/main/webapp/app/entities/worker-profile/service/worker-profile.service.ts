import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkerProfile, NewWorkerProfile } from '../worker-profile.model';

export type PartialUpdateWorkerProfile = Partial<IWorkerProfile> & Pick<IWorkerProfile, 'id'>;

export type EntityResponseType = HttpResponse<IWorkerProfile>;
export type EntityArrayResponseType = HttpResponse<IWorkerProfile[]>;

@Injectable({ providedIn: 'root' })
export class WorkerProfileService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/worker-profiles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(workerProfile: NewWorkerProfile): Observable<EntityResponseType> {
    return this.http.post<IWorkerProfile>(this.resourceUrl, workerProfile, { observe: 'response' });
  }

  update(workerProfile: IWorkerProfile): Observable<EntityResponseType> {
    return this.http.put<IWorkerProfile>(`${this.resourceUrl}/${this.getWorkerProfileIdentifier(workerProfile)}`, workerProfile, {
      observe: 'response',
    });
  }

  partialUpdate(workerProfile: PartialUpdateWorkerProfile): Observable<EntityResponseType> {
    return this.http.patch<IWorkerProfile>(`${this.resourceUrl}/${this.getWorkerProfileIdentifier(workerProfile)}`, workerProfile, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkerProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkerProfile[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWorkerProfileIdentifier(workerProfile: Pick<IWorkerProfile, 'id'>): number {
    return workerProfile.id;
  }

  compareWorkerProfile(o1: Pick<IWorkerProfile, 'id'> | null, o2: Pick<IWorkerProfile, 'id'> | null): boolean {
    return o1 && o2 ? this.getWorkerProfileIdentifier(o1) === this.getWorkerProfileIdentifier(o2) : o1 === o2;
  }

  addWorkerProfileToCollectionIfMissing<Type extends Pick<IWorkerProfile, 'id'>>(
    workerProfileCollection: Type[],
    ...workerProfilesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workerProfiles: Type[] = workerProfilesToCheck.filter(isPresent);
    if (workerProfiles.length > 0) {
      const workerProfileCollectionIdentifiers = workerProfileCollection.map(
        workerProfileItem => this.getWorkerProfileIdentifier(workerProfileItem)!
      );
      const workerProfilesToAdd = workerProfiles.filter(workerProfileItem => {
        const workerProfileIdentifier = this.getWorkerProfileIdentifier(workerProfileItem);
        if (workerProfileCollectionIdentifiers.includes(workerProfileIdentifier)) {
          return false;
        }
        workerProfileCollectionIdentifiers.push(workerProfileIdentifier);
        return true;
      });
      return [...workerProfilesToAdd, ...workerProfileCollection];
    }
    return workerProfileCollection;
  }
}
