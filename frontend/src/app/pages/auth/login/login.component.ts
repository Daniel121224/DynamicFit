import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; // 👈 Importar Router
import { AuthService } from '../../../services/auth.service';
import { LoginRequest } from '../../../models/login-request.model';
import Swal from 'sweetalert2';


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

  mostrarAlertaLogin(): void {
    Swal.fire({
      icon: 'success',
      title: '¡Bienvenido!',
      text: 'Has iniciado sesión correctamente.',
      timer: 2000,
      showConfirmButton: false
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
          localStorage.setItem('id_usuario', response.usuario.id.toString());
          localStorage.setItem('id_carrito', response.usuario.carrito.id_carrito.toString());
          localStorage.setItem('rol', response.usuario.rol);

          this.mostrarAlertaLogin(); // ✅ Mostrar alerta
          this.router.navigateByUrl('/catalogo');
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
