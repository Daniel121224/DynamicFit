export interface Producto {
    id_producto: number;
    nombre_producto: string;
    descripcion: string;
    precio: number;
    categoria: string;
    imagenUrl?: string; // opcional por ahora
  }
  

  /* //Futuramente para saber a que usuarios o carritos se encuentran
  export interface CarritoProducto {
    id_carrito_producto: number;
    cantidad: number;
  }
  
  export interface Producto {
    id_producto: number;
    nombre_producto: string;
    descripcion: string;
    precio: number;
    categoria: string;
    carritoProductos?: CarritoProducto[];
  }
    */