import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWorkerProfileAttencionChannel } from '../worker-profile-attencion-channel.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../worker-profile-attencion-channel.test-samples';

import { WorkerProfileAttencionChannelService } from './worker-profile-attencion-channel.service';

const requireRestSample: IWorkerProfileAttencionChannel = {
  ...sampleWithRequiredData,
};

describe('WorkerProfileAttencionChannel Service', () => {
  let service: WorkerProfileAttencionChannelService;
  let httpMock: HttpTestingController;
  let expectedResult: IWorkerProfileAttencionChannel | IWorkerProfileAttencionChannel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkerProfileAttencionChannelService);
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

    it('should create a WorkerProfileAttencionChannel', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const workerProfileAttencionChannel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(workerProfileAttencionChannel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkerProfileAttencionChannel', () => {
      const workerProfileAttencionChannel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(workerProfileAttencionChannel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkerProfileAttencionChannel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkerProfileAttencionChannel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WorkerProfileAttencionChannel', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWorkerProfileAttencionChannelToCollectionIfMissing', () => {
      it('should add a WorkerProfileAttencionChannel to an empty array', () => {
        const workerProfileAttencionChannel: IWorkerProfileAttencionChannel = sampleWithRequiredData;
        expectedResult = service.addWorkerProfileAttencionChannelToCollectionIfMissing([], workerProfileAttencionChannel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workerProfileAttencionChannel);
      });

      it('should not add a WorkerProfileAttencionChannel to an array that contains it', () => {
        const workerProfileAttencionChannel: IWorkerProfileAttencionChannel = sampleWithRequiredData;
        const workerProfileAttencionChannelCollection: IWorkerProfileAttencionChannel[] = [
          {
            ...workerProfileAttencionChannel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorkerProfileAttencionChannelToCollectionIfMissing(
          workerProfileAttencionChannelCollection,
          workerProfileAttencionChannel
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkerProfileAttencionChannel to an array that doesn't contain it", () => {
        const workerProfileAttencionChannel: IWorkerProfileAttencionChannel = sampleWithRequiredData;
        const workerProfileAttencionChannelCollection: IWorkerProfileAttencionChannel[] = [sampleWithPartialData];
        expectedResult = service.addWorkerProfileAttencionChannelToCollectionIfMissing(
          workerProfileAttencionChannelCollection,
          workerProfileAttencionChannel
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workerProfileAttencionChannel);
      });

      it('should add only unique WorkerProfileAttencionChannel to an array', () => {
        const workerProfileAttencionChannelArray: IWorkerProfileAttencionChannel[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const workerProfileAttencionChannelCollection: IWorkerProfileAttencionChannel[] = [sampleWithRequiredData];
        expectedResult = service.addWorkerProfileAttencionChannelToCollectionIfMissing(
          workerProfileAttencionChannelCollection,
          ...workerProfileAttencionChannelArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workerProfileAttencionChannel: IWorkerProfileAttencionChannel = sampleWithRequiredData;
        const workerProfileAttencionChannel2: IWorkerProfileAttencionChannel = sampleWithPartialData;
        expectedResult = service.addWorkerProfileAttencionChannelToCollectionIfMissing(
          [],
          workerProfileAttencionChannel,
          workerProfileAttencionChannel2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workerProfileAttencionChannel);
        expect(expectedResult).toContain(workerProfileAttencionChannel2);
      });

      it('should accept null and undefined values', () => {
        const workerProfileAttencionChannel: IWorkerProfileAttencionChannel = sampleWithRequiredData;
        expectedResult = service.addWorkerProfileAttencionChannelToCollectionIfMissing([], null, workerProfileAttencionChannel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workerProfileAttencionChannel);
      });

      it('should return initial array if no WorkerProfileAttencionChannel is added', () => {
        const workerProfileAttencionChannelCollection: IWorkerProfileAttencionChannel[] = [sampleWithRequiredData];
        expectedResult = service.addWorkerProfileAttencionChannelToCollectionIfMissing(
          workerProfileAttencionChannelCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(workerProfileAttencionChannelCollection);
      });
    });

    describe('compareWorkerProfileAttencionChannel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWorkerProfileAttencionChannel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWorkerProfileAttencionChannel(entity1, entity2);
        const compareResult2 = service.compareWorkerProfileAttencionChannel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWorkerProfileAttencionChannel(entity1, entity2);
        const compareResult2 = service.compareWorkerProfileAttencionChannel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWorkerProfileAttencionChannel(entity1, entity2);
        const compareResult2 = service.compareWorkerProfileAttencionChannel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
