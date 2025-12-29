import { Routes } from '@angular/router';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { AdminComponent } from './features/admin/admin.component';
import { LoginComponent } from './features/login/login.component';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
    { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
    { path: 'admin', component: AdminComponent, canActivate: [authGuard, adminGuard] },
    { path: '**', redirectTo: '/dashboard' }
];
