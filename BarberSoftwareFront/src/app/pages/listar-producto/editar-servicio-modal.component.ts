import { Component, Input, Output, EventEmitter, OnInit, signal } from '@angular/core';
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
  public categorias: Categoria[] = [];
  public imagePreview = signal<string | null>(null);

  constructor(
    private servicioService: ServicioService,
    private categoriaService: categoriaService
  ) {}


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

  public onFileChange(event: any): void {
    const file = event.target.files[0];
    if (!file) {
      // Si el usuario cancela, mantenemos la imagen original (si existe)
      this.servicio.imagenBase64 = this.imagePreview(); 
      return;
    }

    // Validación de tipo de archivo
    if (!file.type.startsWith('image/')) {
      Swal.fire('Archivo inválido', 'Por favor, seleccione un archivo de imagen.', 'error');
      event.target.value = null;
      return;
    }
    
    // Validación de tamaño (2MB)
    const maxSizeInBytes = 4 * 1024 * 1024; // 4MB
    if (file.size > maxSizeInBytes) {
       Swal.fire('Archivo muy grande', `La imagen no puede pesar más de 2MB.`, 'error');
       event.target.value = null;
       return;
    }

    const reader = new FileReader();
    reader.onload = (e: any) => {
      // Asignamos el Base64 al servicio y a la vista previa
      this.servicio.imagenBase64 = e.target.result; 
      this.imagePreview.set(e.target.result); 
    };
    reader.onerror = (error) => {
        console.error('Error leyendo el archivo:', error);
        Swal.fire('Error', 'No se pudo leer el archivo de imagen.', 'error');
    };
    reader.readAsDataURL(file); // Inicia la conversión
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
