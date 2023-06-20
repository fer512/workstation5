import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap, RouterStateSnapshot } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IWaitingRoomAttencionChannel } from '../waiting-room-attencion-channel.model';
import { WaitingRoomAttencionChannelService } from '../service/waiting-room-attencion-channel.service';

import waitingRoomAttencionChannelResolve from './waiting-room-attencion-channel-routing-resolve.service';

describe('WaitingRoomAttencionChannel routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: WaitingRoomAttencionChannelService;
  let resultWaitingRoomAttencionChannel: IWaitingRoomAttencionChannel | null | undefined;

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
    service = TestBed.inject(WaitingRoomAttencionChannelService);
    resultWaitingRoomAttencionChannel = undefined;
  });

  describe('resolve', () => {
    it('should return IWaitingRoomAttencionChannel returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        waitingRoomAttencionChannelResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultWaitingRoomAttencionChannel = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWaitingRoomAttencionChannel).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        waitingRoomAttencionChannelResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultWaitingRoomAttencionChannel = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultWaitingRoomAttencionChannel).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IWaitingRoomAttencionChannel>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        waitingRoomAttencionChannelResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultWaitingRoomAttencionChannel = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWaitingRoomAttencionChannel).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
