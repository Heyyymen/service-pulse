import { Component, Input } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common'; // Angular 18 Standalone usually imports CommonModule or specific pipes
import { Service } from '../../../core/models/service.model';

@Component({
  selector: 'app-service-card',
  standalone: true,
  imports: [DatePipe, CommonModule],
  templateUrl: './service-card.component.html',
  styleUrl: './service-card.component.css'
})
export class ServiceCardComponent {
  @Input({ required: true }) service!: Service;

  getStatusColor(status: string): string {
    switch (status) {
      case 'OPERATIONAL': return 'bg-green-100 text-green-800 border-green-200';
      case 'DEGRADED': return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'MAINTENANCE': return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'OUTAGE': return 'bg-red-100 text-red-800 border-red-200';
      default: return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  }

  getStatusIcon(status: string): string {
    // Simple unicode or implementation details later if using icons
    switch (status) {
      case 'OPERATIONAL': return '●';
      case 'DEGRADED': return '⚠';
      case 'MAINTENANCE': return '🛠';
      case 'OUTAGE': return '✖';
      default: return '?';
    }
  }
}
