import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap, RouterStateSnapshot } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IWorkerProfileAttencionChannel } from '../worker-profile-attencion-channel.model';
import { WorkerProfileAttencionChannelService } from '../service/worker-profile-attencion-channel.service';

import workerProfileAttencionChannelResolve from './worker-profile-attencion-channel-routing-resolve.service';

describe('WorkerProfileAttencionChannel routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: WorkerProfileAttencionChannelService;
  let resultWorkerProfileAttencionChannel: IWorkerProfileAttencionChannel | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(WorkerProfileAttencionChannelService);
    resultWorkerProfileAttencionChannel = undefined;
  });

  describe('resolve', () => {
    it('should return IWorkerProfileAttencionChannel returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        workerProfileAttencionChannelResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultWorkerProfileAttencionChannel = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWorkerProfileAttencionChannel).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        workerProfileAttencionChannelResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultWorkerProfileAttencionChannel = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultWorkerProfileAttencionChannel).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IWorkerProfileAttencionChannel>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        workerProfileAttencionChannelResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultWorkerProfileAttencionChannel = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWorkerProfileAttencionChannel).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
