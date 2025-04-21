// src/app/models/login-response.model.ts
import { Usuario } from './usuario.model';

export interface LoginResponse {
  usuario: Usuario | null;
  mensaje: string;
  statusCode: number;
}
