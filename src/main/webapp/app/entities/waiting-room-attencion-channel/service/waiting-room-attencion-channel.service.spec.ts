import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWaitingRoomAttencionChannel } from '../waiting-room-attencion-channel.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../waiting-room-attencion-channel.test-samples';

import { WaitingRoomAttencionChannelService } from './waiting-room-attencion-channel.service';

const requireRestSample: IWaitingRoomAttencionChannel = {
  ...sampleWithRequiredData,
};

describe('WaitingRoomAttencionChannel Service', () => {
  let service: WaitingRoomAttencionChannelService;
  let httpMock: HttpTestingController;
  let expectedResult: IWaitingRoomAttencionChannel | IWaitingRoomAttencionChannel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WaitingRoomAttencionChannelService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a WaitingRoomAttencionChannel', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const waitingRoomAttencionChannel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(waitingRoomAttencionChannel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WaitingRoomAttencionChannel', () => {
      const waitingRoomAttencionChannel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(waitingRoomAttencionChannel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WaitingRoomAttencionChannel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WaitingRoomAttencionChannel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WaitingRoomAttencionChannel', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWaitingRoomAttencionChannelToCollectionIfMissing', () => {
      it('should add a WaitingRoomAttencionChannel to an empty array', () => {
        const waitingRoomAttencionChannel: IWaitingRoomAttencionChannel = sampleWithRequiredData;
        expectedResult = service.addWaitingRoomAttencionChannelToCollectionIfMissing([], waitingRoomAttencionChannel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(waitingRoomAttencionChannel);
      });

      it('should not add a WaitingRoomAttencionChannel to an array that contains it', () => {
        const waitingRoomAttencionChannel: IWaitingRoomAttencionChannel = sampleWithRequiredData;
        const waitingRoomAttencionChannelCollection: IWaitingRoomAttencionChannel[] = [
          {
            ...waitingRoomAttencionChannel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWaitingRoomAttencionChannelToCollectionIfMissing(
          waitingRoomAttencionChannelCollection,
          waitingRoomAttencionChannel
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WaitingRoomAttencionChannel to an array that doesn't contain it", () => {
        const waitingRoomAttencionChannel: IWaitingRoomAttencionChannel = sampleWithRequiredData;
        const waitingRoomAttencionChannelCollection: IWaitingRoomAttencionChannel[] = [sampleWithPartialData];
        expectedResult = service.addWaitingRoomAttencionChannelToCollectionIfMissing(
          waitingRoomAttencionChannelCollection,
          waitingRoomAttencionChannel
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(waitingRoomAttencionChannel);
      });

      it('should add only unique WaitingRoomAttencionChannel to an array', () => {
        const waitingRoomAttencionChannelArray: IWaitingRoomAttencionChannel[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const waitingRoomAttencionChannelCollection: IWaitingRoomAttencionChannel[] = [sampleWithRequiredData];
        expectedResult = service.addWaitingRoomAttencionChannelToCollectionIfMissing(
          waitingRoomAttencionChannelCollection,
          ...waitingRoomAttencionChannelArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const waitingRoomAttencionChannel: IWaitingRoomAttencionChannel = sampleWithRequiredData;
        const waitingRoomAttencionChannel2: IWaitingRoomAttencionChannel = sampleWithPartialData;
        expectedResult = service.addWaitingRoomAttencionChannelToCollectionIfMissing(
          [],
          waitingRoomAttencionChannel,
          waitingRoomAttencionChannel2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(waitingRoomAttencionChannel);
        expect(expectedResult).toContain(waitingRoomAttencionChannel2);
      });

      it('should accept null and undefined values', () => {
        const waitingRoomAttencionChannel: IWaitingRoomAttencionChannel = sampleWithRequiredData;
        expectedResult = service.addWaitingRoomAttencionChannelToCollectionIfMissing([], null, waitingRoomAttencionChannel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(waitingRoomAttencionChannel);
      });

      it('should return initial array if no WaitingRoomAttencionChannel is added', () => {
        const waitingRoomAttencionChannelCollection: IWaitingRoomAttencionChannel[] = [sampleWithRequiredData];
        expectedResult = service.addWaitingRoomAttencionChannelToCollectionIfMissing(
          waitingRoomAttencionChannelCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(waitingRoomAttencionChannelCollection);
      });
    });

    describe('compareWaitingRoomAttencionChannel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWaitingRoomAttencionChannel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWaitingRoomAttencionChannel(entity1, entity2);
        const compareResult2 = service.compareWaitingRoomAttencionChannel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWaitingRoomAttencionChannel(entity1, entity2);
        const compareResult2 = service.compareWaitingRoomAttencionChannel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWaitingRoomAttencionChannel(entity1, entity2);
        const compareResult2 = service.compareWaitingRoomAttencionChannel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
