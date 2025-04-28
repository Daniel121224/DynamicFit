import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CarritoDTO } from '../models/carrito.model';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {
  private apiUrl = 'http://localhost:9090/api/carritos';

  constructor(private http: HttpClient) {}

  obtenerCarritoPorUsuario(idUsuario: number): Observable<CarritoDTO> {
    return this.http.get<CarritoDTO>(`${this.apiUrl}/${idUsuario}`);
  }

  agregarProducto(idCarrito: number, producto_id: number, cantidad: number) {
    const body = { producto_id, cantidad };
    return this.http.post(`${this.apiUrl}/${idCarrito}/agregar`, body);
  }

  actualizarCantidad(idCarrito: number, idProducto: number, cantidad: number): Observable<CarritoDTO> {
    return this.http.put<CarritoDTO>(`${this.apiUrl}/${idCarrito}/actualizar/${idProducto}`, { cantidad });
  }
  
  eliminarProducto(idCarrito: number, idProducto: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${idCarrito}/quitar/${idProducto}`);
  }

  vaciarCarrito(idCarrito: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${idCarrito}/vaciar`);
  }

  actualizarCarrito(idCarrito: number, productos: { producto_id: number; cantidad: number; }[]): Observable<CarritoDTO> {
    return this.http.put<CarritoDTO>(`${this.apiUrl}/${idCarrito}/actualizar`, { productos });
  }
  
}
