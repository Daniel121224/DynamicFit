import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  submitted = false;
  mensajeError: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      nombre: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      contrasena: ['', Validators.required],
      telefono: ['', Validators.required],
      direccion: ['', Validators.required],
      rol: ['cliente'] // Por defecto
    });
  }

  onSubmit(): void {
    this.submitted = true;
    this.mensajeError = null;

    if (this.registerForm.invalid) return;

    const nuevoUsuario: Usuario = this.registerForm.value;

    this.authService.register(nuevoUsuario).subscribe({
      next: (usuarioCreado) => {
        console.log('Registro exitoso:', usuarioCreado);
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Error al registrar usuario:', err);
        this.mensajeError = 'Error al registrar. Intenta de nuevo.';
      }
    });
  }
}
