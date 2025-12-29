import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { ServiceApiService } from '../../core/services/service-api.service';
import { Service, ServiceStatus } from '../../core/models/service.model';
import { ServiceCardComponent } from './service-card/service-card.component';
import { Observable, combineLatest, interval, startWith, switchMap, map, shareReplay } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ServiceCardComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  private serviceApi = inject(ServiceApiService);

  searchControl = new FormControl('');
  statusControl = new FormControl<ServiceStatus | 'ALL'>('ALL');

  // Auto-refresh trigger (every 30s)
  private refresh$ = interval(30000).pipe(startWith(0));

  // Base stream of services from API
  private servicesBase$ = this.refresh$.pipe(
    switchMap(() => this.serviceApi.getServices()),
    shareReplay(1)
  );

  // Health summary
  healthSummary$ = this.refresh$.pipe(
    switchMap(() => this.serviceApi.getHealthSummary())
  );

  // Combined filtered stream
  filteredServices$: Observable<Service[]> = combineLatest([
    this.servicesBase$,
    this.searchControl.valueChanges.pipe(startWith('')),
    this.statusControl.valueChanges.pipe(startWith('ALL'))
  ]).pipe(
    map(([services, search, status]) => {
      return services.filter(service => {
        const matchesSearch = service.name.toLowerCase().includes((search || '').toLowerCase()) ||
          service.description.toLowerCase().includes((search || '').toLowerCase());
        const matchesStatus = status === 'ALL' || service.status === status;
        return matchesSearch && matchesStatus;
      });
    })
  );

  ngOnInit(): void {
    // Initial load handled by startWith(0) in refresh$
  }
}
