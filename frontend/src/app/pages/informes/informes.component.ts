import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-informes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './informes.component.html',
  styleUrls: ['./informes.component.css']  // 🔥 corregido: styleUrls[]
})
export class InformesComponent implements OnInit {
  pedidos: any[] = [];
  pedidosFiltrados: any[] = [];
  rol: string | null = null;
  idUsuario: string | null = null;
  activeTab: string = 'tabular';
  private apiUrl = 'http://localhost:9090/api/pedidos';

  precioMin: number | null = null;
  precioMax: number | null = null;
  fechaInicio: string = '';
  fechaFin: string = '';

  totalConsolidado: number = 0;
  estadoMasFrecuente: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.rol = localStorage.getItem('rol');
    this.idUsuario = localStorage.getItem('id_usuario');

    if (this.rol === 'admin') {
      this.obtenerTodosLosPedidos();
    } else {
      this.obtenerPedidosDelUsuario();
    }
  }

  obtenerTodosLosPedidos() {
    this.http.get<any[]>(`${this.apiUrl}`)
      .subscribe({
        next: (data) => {
          this.pedidos = data.map(p => ({ ...p, estadoEditado: p.estado }));
          this.pedidosFiltrados = this.pedidos;
          this.calcularTotalConsolidado();
          this.calcularEstadoMasFrecuente();
        }
        ,
        error: (err) => {
          console.error('Error al obtener pedidos:', err);
        }
      });
  }

  obtenerPedidosDelUsuario() {
    if (!this.idUsuario) return;
    
    this.http.get<any[]>(`${this.apiUrl}/usuario/${this.idUsuario}`)
      .subscribe({
        next: (data) => {
          this.pedidos = data.map(p => ({ ...p, estadoEditado: p.estado }));
          this.pedidosFiltrados = this.pedidos;
          this.calcularTotalConsolidado();
          this.calcularEstadoMasFrecuente();
        }
        ,
        error: (err) => {
          console.error('Error al obtener pedidos del usuario:', err);
        }
      });
  }

  calcularTotalConsolidado() {
    if (this.pedidosFiltrados) {
      this.totalConsolidado = this.pedidosFiltrados.reduce((acc, pedido) => acc + pedido.total_pedido, 0);
    }
  }

  calcularEstadoMasFrecuente() {
    const contadorEstados: { [key: string]: number } = {};
    this.pedidosFiltrados.forEach(pedido => {
      contadorEstados[pedido.estado] = (contadorEstados[pedido.estado] || 0) + 1;
    });

    let estadoFrecuente = '';
    let maxCantidad = 0;

    for (const estado in contadorEstados) {
      if (contadorEstados[estado] > maxCantidad) {
        maxCantidad = contadorEstados[estado];
        estadoFrecuente = estado;
      }
    }

    this.estadoMasFrecuente = estadoFrecuente;
  }

  cambiarTab(tab: string) {
    this.activeTab = tab;
  }

  aplicarFiltros() {
    this.pedidosFiltrados = this.pedidos.filter(pedido => {
      const cumplePrecio = (!this.precioMin || pedido.total_pedido >= this.precioMin) &&
                           (!this.precioMax || pedido.total_pedido <= this.precioMax);

      const fechaPedido = new Date(pedido.fechaPedido);
      const cumpleFecha = (!this.fechaInicio || fechaPedido >= new Date(this.fechaInicio)) &&
                          (!this.fechaFin || fechaPedido <= new Date(this.fechaFin));

      return cumplePrecio && cumpleFecha;
    });

    this.calcularTotalConsolidado();
    this.calcularEstadoMasFrecuente();
  }

  limpiarFiltros() {
    this.precioMin = null;
    this.precioMax = null;
    this.fechaInicio = '';
    this.fechaFin = '';
    this.pedidosFiltrados = [...this.pedidos];

    this.calcularTotalConsolidado();
    this.calcularEstadoMasFrecuente();
  }

  actualizarEstado(pedido: any) {
    const nuevoEstado = pedido.estadoEditado?.trim();
    if (!nuevoEstado) {
      alert('El estado no puede estar vacío.');
      return;
    }
  
    this.http.put(`${this.apiUrl}/${pedido.id_pedido}/estado`, nuevoEstado, {
      headers: { 'Content-Type': 'text/plain' }
    }).subscribe({
      next: (data) => {
        console.log('Estado actualizado exitosamente:', data);
        pedido.estado = nuevoEstado; // Actualizar en pantalla
        delete pedido.estadoEditado; // Limpiar el campo de edición
        alert('Estado actualizado correctamente.');
      },
      error: (err) => {
        console.error('Error al actualizar estado:', err);
        alert('Error al actualizar el estado.');
      }
    });
  }
}
