import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; // 👈 Importar Router
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/login-request.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  submitted = false;
  mensajeError: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router // 👈 Inyectar Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    this.submitted = true;
    this.mensajeError = null;

    if (this.loginForm.invalid) return;

    const loginData: LoginRequest = {
      username: this.loginForm.value.username,
      contrasena: this.loginForm.value.password
    };

    this.authService.login(loginData).subscribe({
      next: (response) => {
        if (response.statusCode === 200 && response.usuario) {
          console.log('Login exitoso:', response.usuario);
          this.router.navigate(['/catalog']); // 👈 Redirigir al catálogo
        } else {
          this.mensajeError = response.mensaje;
        }
      },
      error: (err) => {
        console.error('Error al iniciar sesión:', err);
        this.mensajeError = 'Error al iniciar sesión. Intenta de nuevo.';
      }
    });
  }

  irARegistro(): void {
    this.router.navigate(['/register']);
  }
  
}
