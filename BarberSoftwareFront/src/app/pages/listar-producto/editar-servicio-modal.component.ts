import { Component, Input, Output, EventEmitter, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { Servicio } from '../../core/servicios/modelos/Servicio';
import { ServicioService } from '../../core/servicios/servicios/ServicioService';
import { Categoria } from '../../core/categorias/modelos/Categoria';
import { categoriaService } from '../../core/categorias/servicios/CategoriaService'; 

import Swal from 'sweetalert2';

@Component({
  selector: 'app-editar-servicio-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './editar-servicio-modal.component.html',
  styleUrl: './editar-servicio-modal.component.css'
})
export class EditarServicioModalComponent implements OnInit {

  // --- Inputs y Outputs ---
  // Recibimos una COPIA del servicio para editar
  @Input() servicio!: Servicio; 
  
  // Eventos para notificar al padre (Listar)
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<void>();

  // --- Inyección de Servicios ---
  private servicioService = inject(ServicioService);
  private categoriaService = inject(categoriaService);

  public categorias: Categoria[] = [];

  ngOnInit(): void {
    // 1. Cargar las categorías para el <select>
    this.loadCategorias();

    // 2. Asegurarse de que objCategoria sea un objeto Categoria
    // Si servicio.objCategoria es solo un ID (o nulo), lo buscamos
    if (this.servicio.objCategoria) {
      this.servicio.objCategoria = { id: this.servicio.objCategoria.id, nombre: this.servicio.objCategoria.nombre };
    }
  }

  loadCategorias(): void {
    this.categoriaService.getCategorias().subscribe(
      categorias => this.categorias = categorias
    );
  }

  actualizarServicio(form: NgForm): void {
    if (form.invalid) {
      Swal.fire('Formulario inválido', 'Por favor revise los campos.', 'warning');
      return;
    }

    console.log("Actualizando servicio:", this.servicio);

    this.servicioService.update(this.servicio).subscribe({
      next: (response) => {
        Swal.fire('¡Actualizado!', `El servicio "${response.nombre}" ha sido actualizado.`, 'success');
        this.save.emit(); // Notifica al padre que se guardó
      },
      error: (err) => {
        console.error("Error actualizando servicio:", err);
        Swal.fire('Error', 'No se pudo actualizar el servicio.', 'error');
      }
    });
  }

  cerrarModal(): void {
    this.close.emit(); // Notifica al padre que se cerró
  }

  // Esto es clave para que el <select> compare objetos correctamente
  compararCategorias(c1: Categoria, c2: Categoria): boolean {
    return c1 && c2 ? c1.id === c2.id : c1 === c2;
  }
}
