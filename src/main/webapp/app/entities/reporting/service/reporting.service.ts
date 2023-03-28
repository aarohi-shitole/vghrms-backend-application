import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReporting, NewReporting } from '../reporting.model';

export type PartialUpdateReporting = Partial<IReporting> & Pick<IReporting, 'id'>;

type RestOf<T extends IReporting | NewReporting> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

export type RestReporting = RestOf<IReporting>;

export type NewRestReporting = RestOf<NewReporting>;

export type PartialUpdateRestReporting = RestOf<PartialUpdateReporting>;

export type EntityResponseType = HttpResponse<IReporting>;
export type EntityArrayResponseType = HttpResponse<IReporting[]>;

@Injectable({ providedIn: 'root' })
export class ReportingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reportings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reporting: NewReporting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reporting);
    return this.http
      .post<RestReporting>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reporting: IReporting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reporting);
    return this.http
      .put<RestReporting>(`${this.resourceUrl}/${this.getReportingIdentifier(reporting)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reporting: PartialUpdateReporting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reporting);
    return this.http
      .patch<RestReporting>(`${this.resourceUrl}/${this.getReportingIdentifier(reporting)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReporting>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReporting[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReportingIdentifier(reporting: Pick<IReporting, 'id'>): number {
    return reporting.id;
  }

  compareReporting(o1: Pick<IReporting, 'id'> | null, o2: Pick<IReporting, 'id'> | null): boolean {
    return o1 && o2 ? this.getReportingIdentifier(o1) === this.getReportingIdentifier(o2) : o1 === o2;
  }

  addReportingToCollectionIfMissing<Type extends Pick<IReporting, 'id'>>(
    reportingCollection: Type[],
    ...reportingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportings: Type[] = reportingsToCheck.filter(isPresent);
    if (reportings.length > 0) {
      const reportingCollectionIdentifiers = reportingCollection.map(reportingItem => this.getReportingIdentifier(reportingItem)!);
      const reportingsToAdd = reportings.filter(reportingItem => {
        const reportingIdentifier = this.getReportingIdentifier(reportingItem);
        if (reportingCollectionIdentifiers.includes(reportingIdentifier)) {
          return false;
        }
        reportingCollectionIdentifiers.push(reportingIdentifier);
        return true;
      });
      return [...reportingsToAdd, ...reportingCollection];
    }
    return reportingCollection;
  }

  protected convertDateFromClient<T extends IReporting | NewReporting | PartialUpdateReporting>(reporting: T): RestOf<T> {
    return {
      ...reporting,
      lastModified: reporting.lastModified?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReporting: RestReporting): IReporting {
    return {
      ...restReporting,
      lastModified: restReporting.lastModified ? dayjs(restReporting.lastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReporting>): HttpResponse<IReporting> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReporting[]>): HttpResponse<IReporting[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
