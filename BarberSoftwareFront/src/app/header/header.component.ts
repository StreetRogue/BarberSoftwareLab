import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  host: { 'ngSkipHydration': 'true' }
})
export class HeaderComponent {

   showComingSoon(): void {
    Swal.fire({
      title: '¡Próximamente!',
      text: 'El carrito de compras estará disponible muy pronto.',
      icon: 'info',
      confirmButtonText: 'Entendido',
      confirmButtonColor: 'var(--c-primary-barber, #8a2c3b)' // Usamos el color de nuestra marca
    });
  }

}
