import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { NgApexchartsModule } from 'ng-apexcharts';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-informes',
  standalone: true,
  imports: [CommonModule, FormsModule, NgApexchartsModule],
  templateUrl: './informes.component.html',
  styleUrls: ['./informes.component.css']
})
export class InformesComponent implements OnInit {
  pedidos: any[] = [];
  pedidosFiltrados: any[] = [];
  rol: string | null = null;
  idUsuario: string | null = null;
  id_pedido: number = 0;
  activeTab: string = 'tabular';
  private apiUrl = 'http://localhost:9090/api/pedidos';

  precioMin: number | null = null;
  precioMax: number | null = null;
  fechaInicio: string = '';
  fechaFin: string = '';

  totalConsolidado: number = 0;
  estadoMasFrecuente: string = '';
  consolidadoPorFecha: { fecha: string, total: number }[] = [];


  pedidoSeleccionado: any = null;
  
  // Configuración de ApexCharts
  chartOptions: any = {
    series: [{
      name: "Ventas",
      data: []
    }],
    chart: {
      type: 'bar',
      height: 350,
      toolbar: {
        show: true
      }
    },
    plotOptions: {
      bar: {
        borderRadius: 4,
        horizontal: false,
      }
    },
    dataLabels: {
      enabled: false
    },
    xaxis: {
      categories: [],
      title: {
        text: 'Fecha'
      }
    },
    yaxis: {
      title: {
        text: 'Total de Ventas (COP)'
      },
      labels: {
        formatter: function(val: number) {
          return new Intl.NumberFormat('es-CO', {
            style: 'currency',
            currency: 'COP'
          }).format(val);
        }
      }
    },
    colors: ['#4CAF50'],
    tooltip: {
      y: {
        formatter: function(val: number) {
          return new Intl.NumberFormat('es-CO', {
            style: 'currency',
            currency: 'COP'
          }).format(val);
        }
      }
    }
  };

  pieChartOptions: any = {
    series: [], // Se llenará dinámicamente
    chart: {
      type: 'donut',
      width: 380
    },
    labels: ['Solicitado', 'Despachado', 'Cancelado'],
    colors: ['#4792ff', '#4CAF50', '#FF5C5C'],
    dataLabels: {
      enabled: true,
      formatter: function (val: number) {
        return val.toFixed(1) + '%';
      }
    },
    tooltip: {
      y: {
        formatter: function(val: number) {
          return val.toFixed(0) + " pedidos";
        }
      }
    },
    legend: {
      position: 'bottom'
    }
  };


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
          this.prepararDatosGrafico(); // Añade esta línea
          this.actualizarPieChart(); // Añade esta línea

        },
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
          this.prepararDatosGrafico(); // Añade esta línea
          this.actualizarPieChart(); // Añade esta línea

        },
        error: (err) => {
          console.error('Error al obtener pedidos del usuario:', err);
        }
      });
  }

  calcularTotalConsolidado() {
    const resumen: { [fecha: string]: number } = {};

    this.pedidosFiltrados.forEach(pedido => {
      const fecha = new Date(pedido.fechaPedido).toISOString().split('T')[0]; // solo la fecha
      resumen[fecha] = (resumen[fecha] || 0) + pedido.total_pedido;
    });

    this.consolidadoPorFecha = Object.keys(resumen).map(fecha => ({
      fecha,
      total: resumen[fecha]
    }));

    this.totalConsolidado = this.consolidadoPorFecha.reduce((acc, item) => acc + item.total, 0);
  }

  prepararDatosGrafico() {
    // Ordena las fechas cronológicamente
    this.consolidadoPorFecha.sort((a, b) => new Date(a.fecha).getTime() - new Date(b.fecha).getTime());
    
    const categorias = this.consolidadoPorFecha.map(item => {
      // Formatea la fecha para mostrarla mejor (ej: "28/04/2025")
      const date = new Date(item.fecha);
      return date.toLocaleDateString('es-CO');
    });
    
    const datos = this.consolidadoPorFecha.map(item => item.total);
  
    // Crea un nuevo objeto para forzar la actualización del gráfico
    this.chartOptions = {
      ...this.chartOptions,
      series: [{
        name: "Ventas",
        data: datos
      }],
      xaxis: {
        ...this.chartOptions.xaxis,
        categories: categorias
      }
    };
    
    // Forzar la actualización del gráfico
    this.chartOptions = {...this.chartOptions};
  }

  actualizarPieChart() {
    const estadoCount = {
      Solicitado: 0,
      Despachado: 0,
      Cancelado: 0
    };

    this.pedidosFiltrados.forEach(pedido => {
      if (estadoCount[pedido.estado as keyof typeof estadoCount] !== undefined) {
        estadoCount[pedido.estado as keyof typeof estadoCount]++;
      }
    });

    this.pieChartOptions.series = [
      estadoCount.Solicitado,
      estadoCount.Despachado,
      estadoCount.Cancelado
    ];
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
    this.prepararDatosGrafico(); // Añade esta línea
    this.actualizarPieChart(); // Añade esta línea

  }
  
  limpiarFiltros() {
    this.precioMin = null;
    this.precioMax = null;
    this.fechaInicio = '';
    this.fechaFin = '';
    this.pedidosFiltrados = [...this.pedidos];
  
    this.calcularTotalConsolidado();
    this.calcularEstadoMasFrecuente();
    this.prepararDatosGrafico(); // Añade esta línea
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
        Swal.fire({
          icon: 'success',
          title: 'Actualizado',
          text: 'El estado del pedido fue actualizado exitosamente.',
          timer: 2000,
          showConfirmButton: false
        });
      },
      error: (err) => {
        console.error('Error al actualizar estado:', err);
        alert('Error al actualizar el estado.');
      }
    });
  }


  verDetalles(pedido: any) {
    if (this.pedidoSeleccionado === pedido) {
      this.pedidoSeleccionado = null;
      return;
    }
  
    this.pedidoSeleccionado = pedido;
  
    // Si ya tiene los detalles cargados, no hace nada
    if (pedido.detalles) {
      return;
    }
  
    this.http.get<any>(`${this.apiUrl}/${pedido.id_pedido}`).subscribe({
      next: (pedidoConProductos) => {
        const productos = pedidoConProductos.productos;
  
        if (!productos || productos.length === 0) {
          pedido.detalles = [];
          return;
        }
  
        // Consultar detalles de cada producto
        const peticiones = productos.map((p: any) => {
          return this.http.get<any>(`http://localhost:9090/api/productos/${p.producto_id}`).toPromise().then((productoInfo) => {
            return {
              id_producto: p.producto_id,
              nombre_producto: productoInfo.nombre_producto,
              cantidad: p.cantidad,
              precio: productoInfo.precio
            };
          });
        });
  
        Promise.all(peticiones).then((detallesCompletos) => {
          pedido.detalles = detallesCompletos;
        }).catch(err => {
          console.error('Error obteniendo productos:', err);
          pedido.detalles = [];
        });
      },
      error: (err) => {
        console.error('Error cargando pedido:', err);
        pedido.detalles = [];
      }
    });
  }
  

  eliminarPedido(pedido: any) {
  Swal.fire({
    title: '¿Estás seguro?',
    text: 'Este pedido se eliminará permanentemente.',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Sí, eliminar',
    cancelButtonText: 'Cancelar'
  }).then((result) => {
    if (result.isConfirmed) {
      this.http.delete(`${this.apiUrl}/${pedido.id_pedido}`).subscribe({
        next: () => {
          this.pedidos = this.pedidos.filter(p => p.id_pedido !== pedido.id_pedido);
          this.pedidosFiltrados = this.pedidos;
          this.calcularTotalConsolidado();
          this.calcularEstadoMasFrecuente();
          this.prepararDatosGrafico();
          this.actualizarPieChart();

          setTimeout(() => {
            Swal.fire({
              icon: 'success',
              title: 'Pedido eliminado',
              text: 'El pedido ha sido eliminado exitosamente.',
              timer: 2000,
              showConfirmButton: false
            });
          }, 0);
        },
        error: (err) => {
          console.error('Error al eliminar pedido:', err);
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo eliminar el pedido.',
          });
        }
      });
    }
  });
}



}
