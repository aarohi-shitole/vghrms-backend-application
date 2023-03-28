import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonalId } from '../personal-id.model';
import { PersonalIdService } from '../service/personal-id.service';

@Injectable({ providedIn: 'root' })
export class PersonalIdRoutingResolveService implements Resolve<IPersonalId | null> {
  constructor(protected service: PersonalIdService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonalId | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personalId: HttpResponse<IPersonalId>) => {
          if (personalId.body) {
            return of(personalId.body);
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
