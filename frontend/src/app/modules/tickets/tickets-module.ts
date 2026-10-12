import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { TicketsRoutingModule } from './tickets-routing-module';
import { TicketsPageComponent } from './page/tickets-page.component';
import { TicketCreateComponent } from './create/ticket-create.component';
import { TicketListComponent } from './list/ticket-list.component';

@NgModule({
  declarations: [
    TicketsPageComponent,
    TicketCreateComponent,
    TicketListComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TicketsRoutingModule,
  ]
})
export class TicketsModule { }
