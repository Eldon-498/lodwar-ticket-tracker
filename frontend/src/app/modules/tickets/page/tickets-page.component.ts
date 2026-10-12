import { ChangeDetectorRef, Component, OnInit, ViewChild, inject } from '@angular/core';
import { TicketService } from '../../../services/apis/ticket/ticket.service';
import { TicketStats, TicketStatus } from '../models/ticket.model';
import { TicketListComponent } from '../list/ticket-list.component';

@Component({
  selector: 'lodwar-tickets-page',
  standalone: false,
  templateUrl: './tickets-page.component.html',
  styleUrl: './tickets-page.component.scss'
})
export class TicketsPageComponent implements OnInit {

  private ticketService = inject(TicketService);
  private cdr = inject(ChangeDetectorRef);

  @ViewChild(TicketListComponent) ticketList!: TicketListComponent;

  stats: TicketStats | null = null;

  ngOnInit(): void {

    this.loadStats();
  }

  onTicketCreated(): void {

    this.ticketList?.reload();
    this.loadStats();
  }

  onTicketStatusChanged(): void {

    this.loadStats();
  }

  countFor(status: TicketStatus): number {

    return this.stats?.byStatus?.[status] ?? 0;
  }

  private loadStats(): void {

    this.ticketService.retrieveStats().subscribe({
      next: (stats) => {
        this.stats = stats;
        this.cdr.detectChanges();
      },
      error: () => {}
    });
  }
}
