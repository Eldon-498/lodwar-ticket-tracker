import { Injectable } from '@angular/core';
import { Observable, map, throwError } from 'rxjs';
import { HttpService } from '../../http/http.service';
import {
  ApiResponse,
  CreateTicketRequest,
  SUCCESS_CODE,
  Ticket,
  TicketPage,
  TicketStats,
  TicketStatus,
  UpdateTicketRequest
} from '../../../modules/tickets/models/ticket.model';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  constructor(private hs: HttpService) {}

  public createTicket(data: CreateTicketRequest): Observable<Ticket> {

    return this.hs.makeRequest<ApiResponse<Ticket>>('/tickets', 'POST', data).pipe(
      map(res => this.extract(res))
    );
  }

  public listTickets(status?: TicketStatus, page = 0, size = 20): Observable<TicketPage> {

    const params: any = { page, size };

    if (status) {

      params.status = status;
    }

    return this.hs.makeRequest<ApiResponse<Ticket[]>>('/tickets', 'GET', undefined, { params }).pipe(
      map(res => ({
        content: this.extract(res),
        totalItems: res.totalItems ?? 0,
        totalPages: res.totalPages ?? 0,
        currentPage: res.currentPage ?? 0,
        pageSize: res.pageSize ?? size,
      }))
    );
  }

  public updateTicket(id: number, data: UpdateTicketRequest): Observable<Ticket> {

    return this.hs.makeRequest<ApiResponse<Ticket>>(`/tickets/${id}`, 'PATCH', data).pipe(
      map(res => this.extract(res))
    );
  }

  public retrieveStats(): Observable<TicketStats> {

    return this.hs.makeRequest<ApiResponse<TicketStats>>('/tickets/stats', 'GET').pipe(
      map(res => this.extract(res))
    );
  }

  private extract<T>(res: ApiResponse<T>): T {

    if (res?.status?.code !== SUCCESS_CODE) {

      throw new Error(res?.status?.message ?? 'Request failed');
    }

    return res.data as T;
  }
}
