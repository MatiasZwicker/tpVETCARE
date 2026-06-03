package com.example.PetCare.Controller;

import com.example.PetCare.Service.AuthService;
import com.example.PetCare.model.RolUsuario;
import com.example.PetCare.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Página de inicio pública. Muestra el home de PetCare con hero, servicios,
    // login, registro y sección "Trabajá con nosotros".
    // - Matias Z.
    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    // Función para registrar un usuario común desde el formulario público.
    // Recibe nombre, apellido, email, contraseña y rol (siempre "usuario" desde el frontend).
    // Delega en AuthService.registrar() que solo permite crear usuarios con rol USUARIO.
    // Si hay error, redirige a /#auth y muestra el mensaje en la sección de registro.
    // - Matias Z.
    @PostMapping("/register")
    public String register(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String rol,
            RedirectAttributes redirectAttributes
    ) {
        try {
            RolUsuario rolEnum = mapRol(rol);
            authService.registrar(nombre, apellido, email, password, rolEnum);
            redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorRegistro", e.getMessage());
            return "redirect:/#auth";
        }
    }

    // Función para iniciar sesión.
    // Recibe email, contraseña y rol seleccionado. Verifica que el usuario exista,
    // esté activo, la contraseña coincida y el rol sea correcto.
    // Si los datos son válidos, guarda el objeto Usuario en sesión.
    // Si el usuario tiene debeCambiarPassword = true (profesional aprobado con
    // contraseña default), redirige a /cambiar-password en vez del dashboard.
    // En caso normal, redirige al dashboard correspondiente según el rol.
    // - Matias Z.
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String rol,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            RolUsuario rolEnum = mapRol(rol);
            Usuario usuario = authService.autenticar(email, password, rolEnum);
            session.setAttribute("usuario", usuario);

            if (usuario.isDebeCambiarPassword()) {
                return "redirect:/cambiar-password";
            }

            String dashboardUrl = switch (usuario.getRol()) {
                case USUARIO -> "/dashboard/usuario";
                case ADMINISTRADOR -> "/dashboard/administrador";
                case DUENO -> "/dashboard/dueno";
                case VETERINARIO -> "/dashboard/veterinario";
                case PASEADOR -> "/dashboard/paseador";
                case PELUQUERO -> "/dashboard/peluquero";
                case ADIESTRADOR -> "/dashboard/adiestrador";
                case CUIDADOR -> "/dashboard/cuidador";
            };
            return "redirect:" + dashboardUrl;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorLogin", e.getMessage());
            return "redirect:/#auth";
        }
    }

    // Cierra la sesión del usuario actual invalidando la sesión HTTP.
    // Redirige al home público.
    // - Matias Z.
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); //invalidate destruye / cierra la sesion.
        return "redirect:/";
    }

    // Vista de prueba para verificar el estado de la sesión activa.
    // Muestra los datos del usuario logueado (nombre, email, rol) y el dashboard
    // al que redirigiría según su rol. Si no hay sesión activa, lo indica.
    // - Matias Z.
    @GetMapping("/sesion")
    public String verSesion(HttpSession session, org.springframework.ui.Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("logueado", true);
            model.addAttribute("nombre", usuario.getNombre() + " " + usuario.getApellido());
            model.addAttribute("email", usuario.getEmail());
            model.addAttribute("rol", usuario.getRol());
            model.addAttribute("dashboardUrl", switch (usuario.getRol()) {
                case USUARIO -> "/dashboard/usuario";
                case ADMINISTRADOR -> "/dashboard/administrador";
                case DUENO -> "/dashboard/dueno";
                case VETERINARIO -> "/dashboard/veterinario";
                case PASEADOR -> "/dashboard/paseador";
                case PELUQUERO -> "/dashboard/peluquero";
                case ADIESTRADOR -> "/dashboard/adiestrador";
                case CUIDADOR -> "/dashboard/cuidador";
            });
            model.addAttribute("activo", usuario.isActivo());
        } else {
            model.addAttribute("logueado", false);
        }
        return "sesion";
    }

    // Muestra el formulario para cambiar la contraseña obligatorio.
    // Accede un profesional que inició sesión con la contraseña default.
    // Si el usuario no está logueado o no necesita cambiar la contraseña,
    // redirige al home.
    // - Matias Z.
    @GetMapping("/cambiar-password")
    public String mostrarCambiarPassword(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !usuario.isDebeCambiarPassword()) {
            return "redirect:/";
        }
        return "cambiar-password";
    }

    // Procesa el cambio de contraseña obligatorio.
    // Valida que la nueva contraseña y su confirmación coincidan,
    // actualiza la contraseña en BD y desactiva debeCambiarPassword.
    // Redirige al dashboard correspondiente al rol.
    // - Matias Z.
    @PostMapping("/cambiar-password")
    public String procesarCambiarPassword(
            @RequestParam String nuevaPassword,
            @RequestParam String confirmarPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !usuario.isDebeCambiarPassword()) {
            return "redirect:/";
        }

        if (!nuevaPassword.equals(confirmarPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
            return "redirect:/cambiar-password";
        }

        if (nuevaPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres");
            return "redirect:/cambiar-password";
        }

        authService.cambiarPassword(usuario, nuevaPassword);

        Usuario usuarioActualizado = authService.autenticar(usuario.getEmail(), nuevaPassword, usuario.getRol());
        session.setAttribute("usuario", usuarioActualizado);

        String dashboardUrl = switch (usuarioActualizado.getRol()) {
            case USUARIO -> "/dashboard/usuario";
            case ADMINISTRADOR -> "/dashboard/administrador";
            case DUENO -> "/dashboard/dueno";
            case VETERINARIO -> "/dashboard/veterinario";
            case PASEADOR -> "/dashboard/paseador";
            case PELUQUERO -> "/dashboard/peluquero";
            case ADIESTRADOR -> "/dashboard/adiestrador";
            case CUIDADOR -> "/dashboard/cuidador";
        };
        return "redirect:" + dashboardUrl;
    }

    @GetMapping("/dashboard/administrador")
    public String dashboardAdministrador(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        return "dashboard-administrador";
    }

    @GetMapping("/dashboard/dueno")
    public String dashboardDueno(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        return "dashboard-dueno";
    }

    @GetMapping("/dashboard/veterinario")
    public String dashboardVeterinario(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        return "dashboard-veterinario";
    }

    @GetMapping("/dashboard/paseador")
    public String dashboardPaseador(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        return "dashboard-paseador";
    }

    @GetMapping("/dashboard/peluquero")
    public String dashboardPeluquero(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        return "dashboard-peluquero";
    }

    @GetMapping("/dashboard/adiestrador")
    public String dashboardAdiestrador(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        return "dashboard-adiestrador";
    }

    @GetMapping("/dashboard/cuidador")
    public String dashboardCuidador(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        return "dashboard-cuidador";
    }

    @GetMapping("/dashboard/usuario")
    public String dashboardUsuario(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        return "dashboard-usuario";
    }

    // Convierte un string de rol (enviado desde el formulario) al enum RolUsuario.
    // Se usa tanto en login como en register. Lanza excepción si el string no coincide
    // con ningún valor válido.
    // - Matias Z.
    private RolUsuario mapRol(String rol) {
        return switch (rol.toLowerCase()) {
            case "usuario" -> RolUsuario.USUARIO;
            case "administrador" -> RolUsuario.ADMINISTRADOR;
            case "dueno" -> RolUsuario.DUENO;
            case "duenio" -> RolUsuario.DUENO;
            case "dueñio" -> RolUsuario.DUENO;
            case "veterinario" -> RolUsuario.VETERINARIO;
            case "paseador" -> RolUsuario.PASEADOR;
            case "peluquero" -> RolUsuario.PELUQUERO;
            case "adiestrador" -> RolUsuario.ADIESTRADOR;
            case "cuidador" -> RolUsuario.CUIDADOR;
            default -> throw new IllegalArgumentException("Rol inválido");
        };
    }
}
