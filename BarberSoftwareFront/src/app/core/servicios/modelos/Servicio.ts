import { Categoria } from "../../categorias/modelos/Categoria";

export class Servicio {
  id!: number;
  nombre!: string;
  descripcion!: string;
  precio!: number;
  duracionMinutos!: number;
  fechaCreacion!: string;
  objCategoria: Categoria | null = null;
}