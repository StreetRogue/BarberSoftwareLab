import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common'; // Para *ngFor
import { FormsModule } from '@angular/forms';   // Para [(ngModel)]
import { Router, RouterLink } from '@angular/router';
import Swal from 'sweetalert2'; // Para las alertas dinámicas

// Importaciones corregidas según tu estructura de carpetas
import { Categoria } from '../../core/categorias/modelos/Categoria';
import { categoriaService } from '../../core/categorias/servicios/CategoriaService'; 
import { Servicio } from '../../core/servicios/modelos/Servicio';
import { ServicioService } from '../../core/servicios/servicios/ServicioService';

@Component({
  selector: 'app-registrar-producto',
  standalone: true,
  imports: [
    CommonModule, // Necesario para @for
    FormsModule,  // Necesario para [(ngModel)]
    RouterLink
  ],
  templateUrl: './registrar-producto.component.html',
  styleUrl: './registrar-producto.component.css'
})
export class RegistrarProductoComponent implements OnInit {

  // Lógica del Stepper
  public currentStep = signal<number>(1);


  public servicio: Servicio = new Servicio();
  public categorias: Categoria[] = [];
  public titulo: string = 'Crear Servicio'; 

  constructor(
    private categoriaService: categoriaService,
    private servicioService: ServicioService, 
    private router: Router
  ) { }

  ngOnInit(): void {
    // Inicializamos el objeto (esto es opcional, ya se hace en el constructor de Servicio)
    this.servicio.objCategoria = null;
    
    // Cargamos las categorías desde tu servicio
    this.categoriaService.getCategorias().subscribe(
      (categorias) => this.categorias = categorias
    );
  }

  // --- Métodos de navegación del Stepper ---
  nextStep() {
    if (this.currentStep() < 3) {
      this.currentStep.set(this.currentStep() + 1);
    }
  }

  prevStep() {
    if (this.currentStep() > 1) {
      this.currentStep.set(this.currentStep() - 1);
    }
  }

  
  public crearServicio(formServicio: any): void {
    
    // Comprobación final
    if (formServicio.invalid) {
      Swal.fire('Error', 'Por favor, complete todos los campos requeridos.', 'error');
      return;
    }

    console.log("Creando servicio");
    this.servicioService.create(this.servicio).subscribe(
      {
        next: (response) => {
          console.log("Servicio creado exitosamente");
          console.log(response);
          this.router.navigate(['/admin']); // Navegamos de vuelta al panel de admin
          Swal.fire(
            'Nuevo Servicio',
            `¡Servicio "${response.nombre}" creado con éxito!`, 
            'success'
          );
        },
        error: (err) => {
          console.error('Error al crear servicio:', err.message);
          Swal.fire('Error', 'Hubo un problema al crear el servicio.', 'error');
        }
      }
    )
  }
}

