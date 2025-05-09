import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { CarritoService } from '../../services/carrito.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-producto',
  templateUrl: './producto.component.html',
  styleUrls: ['./producto.component.css'],
  standalone: true,
  imports: [[CommonModule, FormsModule]]
})
export class ProductoComponent implements OnInit {
  producto!: Producto;
  cantidad: number = 1;
  tabSeleccionado: string = 'entrega';


  constructor(
    private route: ActivatedRoute,
    private productoService: ProductoService,
    private carritoService: CarritoService,
    //private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productoService.obtenerProductoPorId(id).subscribe({
      next: (data) => {
        this.producto = {
          ...data,
          imagenUrl: `assets/productos/${data.nombre_producto}.png` // Asignar imagen temporal
        };
      },
      error: (err) => console.error('Error al cargar producto:', err)
    });
  }

  /*
  agregarAlCarrito() {
    const idCarrito = Number(localStorage.getItem('id_carrito'));
    this.carritoService.agregarProducto(idCarrito, this.producto.id_producto, this.cantidad)
      .subscribe({
        next: (res) => alert('Producto agregado correctamente.'),
        error: (err) => alert('Error al agregar al carrito')
      });
  }
      */

  //Alerta bonita con sweetalert2
  agregarAlCarrito() {
    const idCarrito = Number(localStorage.getItem('id_carrito'));
    this.carritoService.agregarProducto(idCarrito, this.producto.id_producto, this.cantidad)
      .subscribe({
        next: (res) => {
          Swal.fire({
            icon: 'success',
            title: 'Producto agregado',
            text: `${this.producto.nombre_producto} fue agregado al carrito.`,
            timer: 2000,
            showConfirmButton: false
          });
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo agregar el producto al carrito.',
          });
        }
      });
  }


}
