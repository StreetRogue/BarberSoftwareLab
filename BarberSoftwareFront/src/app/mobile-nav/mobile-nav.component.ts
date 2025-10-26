import { Component } from '@angular/core';
// 1. Importamos RouterLink y RouterLinkActive
import { RouterLink, RouterLinkActive } from '@angular/router'; 
// 2. Importamos SweetAlert2
import Swal from 'sweetalert2';

@Component({
  selector: 'app-mobile-nav',
  standalone: true,
  // 3. Añadimos RouterLinkActive a los imports
  imports: [RouterLink, RouterLinkActive], 
  templateUrl: './mobile-nav.component.html',
  styleUrl: './mobile-nav.component.css'
})
export class MobileNavComponent {

  // Método para el popup "En Construcción"
  showEnConstruccion(): void {
    Swal.fire({
      title: '¡Próximamente!',
      text: 'La agenda de citas estará disponible muy pronto.',
      icon: 'info',
      confirmButtonText: 'Entendido',
      confirmButtonColor: 'var(--c-primary-barber, #8a2c3b)' // Usamos el color de nuestra marca
    });
  }
}

