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

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

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
            return "redirect:/#register";
        }
    }

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

            String dashboardUrl = switch (usuario.getRol()) {
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
            return "redirect:/#login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
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

    private RolUsuario mapRol(String rol) {
        return switch (rol.toLowerCase()) {
            case "administrador" -> RolUsuario.ADMINISTRADOR;
            case "dueno" -> RolUsuario.DUENO;
            case "veterinario" -> RolUsuario.VETERINARIO;
            case "paseador" -> RolUsuario.PASEADOR;
            case "peluquero" -> RolUsuario.PELUQUERO;
            case "adiestrador" -> RolUsuario.ADIESTRADOR;
            case "cuidador" -> RolUsuario.CUIDADOR;
            default -> throw new IllegalArgumentException("Rol inválido");
        };
    }
}
