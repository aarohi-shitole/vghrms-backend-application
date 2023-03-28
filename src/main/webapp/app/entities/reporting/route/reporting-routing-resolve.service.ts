import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReporting } from '../reporting.model';
import { ReportingService } from '../service/reporting.service';

@Injectable({ providedIn: 'root' })
export class ReportingRoutingResolveService implements Resolve<IReporting | null> {
  constructor(protected service: ReportingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReporting | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((reporting: HttpResponse<IReporting>) => {
          if (reporting.body) {
            return of(reporting.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
