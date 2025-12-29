import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ServiceApiService } from '../../core/services/service-api.service';
import { Service, ServiceStatus } from '../../core/models/service.model';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent implements OnInit {
  private serviceApi = inject(ServiceApiService);
  private fb = inject(FormBuilder);

  services: Service[] = [];
  loading = false;
  showForm = false;
  editingId: number | null = null;
  
  serviceForm: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    description: ['', [Validators.maxLength(500)]],
    status: ['OPERATIONAL', Validators.required],
    maintenanceMessage: ['', [Validators.maxLength(1000)]]
  });

  statusOptions: ServiceStatus[] = ['OPERATIONAL', 'DEGRADED', 'MAINTENANCE', 'OUTAGE'];
  
  message: { type: 'success' | 'error', text: string } | null = null;

  ngOnInit(): void {
    this.loadServices();
  }

  loadServices(): void {
    this.loading = true;
    this.serviceApi.getServices().subscribe({
      next: (data) => {
        this.services = data;
        this.loading = false;
      },
      error: (err) => {
        this.showMessage('error', 'Failed to load services');
        this.loading = false;
      }
    });
  }

  openCreateForm(): void {
    this.showForm = true;
    this.editingId = null;
    this.serviceForm.reset({ status: 'OPERATIONAL' });
  }

  openEditForm(service: Service): void {
    this.showForm = true;
    this.editingId = service.id!;
    this.serviceForm.patchValue(service);
  }

  closeForm(): void {
    this.showForm = false;
    this.editingId = null;
    this.serviceForm.reset();
  }

  onSubmit(): void {
    if (this.serviceForm.invalid) return;

    this.loading = true;
    const formValue = this.serviceForm.value;

    const operation = this.editingId
      ? this.serviceApi.updateService(this.editingId, formValue)
      : this.serviceApi.createService(formValue);

    operation.subscribe({
      next: () => {
        this.showMessage('success', this.editingId ? 'Service updated successfully' : 'Service created successfully');
        this.closeForm();
        this.loadServices();
      },
      error: (err) => {
        this.showMessage('error', 'Operation failed: ' + (err.error?.message || err.message));
        this.loading = false;
      }
    });
  }

  deleteService(id: number, name: string): void {
    if (!confirm(`Are you sure you want to delete "${name}"?`)) return;

    this.loading = true;
    this.serviceApi.deleteService(id).subscribe({
      next: () => {
        this.showMessage('success', 'Service deleted successfully');
        this.loadServices();
      },
      error: (err) => {
        this.showMessage('error', 'Failed to delete service');
        this.loading = false;
      }
    });
  }

  quickStatusChange(service: Service, newStatus: ServiceStatus): void {
    this.serviceApi.updateServiceStatus(service.id!, newStatus, service.maintenanceMessage).subscribe({
      next: () => {
        this.showMessage('success', 'Status updated successfully');
        this.loadServices();
      },
      error: () => {
        this.showMessage('error', 'Failed to update status');
      }
    });
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'OPERATIONAL': return 'bg-green-100 text-green-800';
      case 'DEGRADED': return 'bg-yellow-100 text-yellow-800';
      case 'MAINTENANCE': return 'bg-blue-100 text-blue-800';
      case 'OUTAGE': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  private showMessage(type: 'success' | 'error', text: string): void {
    this.message = { type, text };
    setTimeout(() => this.message = null, 5000);
  }
}
