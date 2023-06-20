import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBranch } from '../branch.model';
import { BranchService } from '../service/branch.service';

export const branchResolve = (route: ActivatedRouteSnapshot): Observable<null | IBranch> => {
  const id = route.params['id'];
  if (id) {
    return inject(BranchService)
      .find(id)
      .pipe(
        mergeMap((branch: HttpResponse<IBranch>) => {
          if (branch.body) {
            return of(branch.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default branchResolve;
