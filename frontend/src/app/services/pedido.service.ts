import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PedidoService {
  private apiUrl = 'http://localhost:9090/api/pedidos';

  constructor(private http: HttpClient) {}

  generarPedido(idUsuario: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/crear/${idUsuario}`, {});
  }

  eliminarPedido(idPedido: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${idPedido}`);
  }
}
