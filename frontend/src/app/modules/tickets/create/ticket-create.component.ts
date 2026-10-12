import { ChangeDetectorRef, Component, EventEmitter, Output, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { finalize } from 'rxjs';

import { TicketService } from '../../../services/apis/ticket/ticket.service';
import { TICKET_PRIORITIES, TicketPriority } from '../models/ticket.model';

@Component({
  selector: 'lodwar-ticket-create',
  standalone: false,
  templateUrl: './ticket-create.component.html',
  styleUrl: './ticket-create.component.scss'
})
export class TicketCreateComponent {

  private fb = inject(FormBuilder);
  private ticketService = inject(TicketService);
  private toastr = inject(ToastrService);
  private cdr = inject(ChangeDetectorRef);

  @Output() created = new EventEmitter<void>();

  priorities = TICKET_PRIORITIES;

  isSubmitting = false;

  form: FormGroup = this.fb.group({
    title: ['', [Validators.required, Validators.maxLength(200)]],
    description: ['', [Validators.maxLength(2000)]],
    priority: ['MEDIUM' as TicketPriority, [Validators.required]],
  });

  get title() { return this.form.get('title'); }

  submit(): void {

    if (this.form.invalid) {

      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;

    const payload = {
      title: this.form.value.title.trim(),
      description: this.form.value.description?.trim() || null,
      priority: this.form.value.priority as TicketPriority,
    };

    this.ticketService.createTicket(payload).pipe(
      finalize(() => {
        this.isSubmitting = false;
        this.cdr.detectChanges();
      })
    ).subscribe({
      next: () => {
        this.toastr.success('Ticket created');
        this.form.reset({ title: '', description: '', priority: 'MEDIUM' });
        this.created.emit();
      },
      error: (err) => {
        const message = err?.error?.message ?? 'Failed to create ticket';
        this.toastr.error(message);
      }
    });
  }
}
