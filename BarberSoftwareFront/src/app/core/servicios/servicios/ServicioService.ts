import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Servicio } from '../modelos/Servicio'; 

@Injectable({
  providedIn: 'root'
})
export class ServicioService {
  private httpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
  private urlEndPoint: string = 'http://localhost:5000/api/servicios';

  constructor(private http: HttpClient) { }

  getServicios(): Observable<Servicio[]> {
    return this.http.get<Servicio[]>(this.urlEndPoint);
  }

  getServiciosPorCategoria(idCategoria: number): Observable<Servicio[]> {
    return this.http.get<Servicio[]>(`${this.urlEndPoint}/categoria/${idCategoria}`);
  }

  create(servicio: Servicio): Observable<Servicio> {
    return this.http.post<Servicio>(this.urlEndPoint, servicio, { headers: this.httpHeaders });
  }

  update(servicio: Servicio): Observable<Servicio> {
    return this.http.put<Servicio>(`${this.urlEndPoint}/${servicio.id}`, servicio, { headers: this.httpHeaders });
  }

  deleteServicio(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlEndPoint}/${id}`, { headers: this.httpHeaders });
  }

  getServicioById(id: number): Observable<Servicio> {
    return this.http.get<Servicio>(`${this.urlEndPoint}/${id}`);
  }
}
