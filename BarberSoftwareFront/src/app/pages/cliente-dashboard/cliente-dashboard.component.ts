import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-cliente-dashboard',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="container text-center py-5 my-5">
      <h1 class="display-4">¡Bienvenido, Cliente!</h1>
      <p class="lead text-muted">Este sería tu panel personal para ver citas y productos.</p>
      <a routerLink="/" class="btn btn-outline-dark mt-4">
        <i class="bi bi-arrow-left me-2"></i> Volver al Inicio
      </a>
    </div>
  `
})
export class ClienteDashboardComponent {}
