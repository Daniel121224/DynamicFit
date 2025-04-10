import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../services/auth.service'; // asegúrate de que la ruta sea correcta

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  submitted = false;
  loading = false;
  loginError = '';

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    this.submitted = true;
    this.loginError = '';

    if (this.loginForm.invalid) return;

    this.loading = true;

    const { username, contrasena } = this.loginForm.value;

    this.authService.login(username, contrasena).subscribe({
      next: (res) => {
        if (res.statusCode === 200) {
          alert('Inicio de sesión exitoso 🎉');
          console.log('Usuario logueado:', res.usuario);
          // Puedes guardar el usuario en localStorage o navegar a otro componente
        } else {
          this.loginError = res.mensaje || 'Credenciales inválidas';
        }
        this.loading = false;
      },
      error: (err) => {
        this.loginError = err.error?.mensaje || 'Error del servidor';
        this.loading = false;
      }
    });
  }
}
