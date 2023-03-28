import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFamilyInfo } from '../family-info.model';
import { FamilyInfoService } from '../service/family-info.service';

@Injectable({ providedIn: 'root' })
export class FamilyInfoRoutingResolveService implements Resolve<IFamilyInfo | null> {
  constructor(protected service: FamilyInfoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFamilyInfo | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((familyInfo: HttpResponse<IFamilyInfo>) => {
          if (familyInfo.body) {
            return of(familyInfo.body);
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
