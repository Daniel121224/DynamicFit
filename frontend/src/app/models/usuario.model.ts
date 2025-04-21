// src/app/models/usuario.model.ts
export interface Usuario {
    id: number;
    username: string;
    nombre: string;
    email: string;
    telefono: string;
    direccion: string;
    rol: string;
    carrito: {
      id_carrito: number;
      total_carrito: number;
      usuario_id: number;
      productos: any[];
    };
  }
  