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
                case DUENO -> "/dashboard/dueno";
                case USUARIO -> "/dashboard/usuario";
                case VETERINARIA -> "/dashboard/veterinaria";
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

    @GetMapping("/dashboard/dueno")
    public String dashboardDueno(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        return "dashboard-dueno";
    }

    @GetMapping("/dashboard/usuario")
    public String dashboardUsuario(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        return "dashboard-usuario";
    }

    @GetMapping("/dashboard/veterinaria")
    public String dashboardVeterinaria(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        return "dashboard-veterinario";
    }

    private RolUsuario mapRol(String rol) {
        return switch (rol.toLowerCase()) {
            case "dueno" -> RolUsuario.DUENO;
            case "usuario" -> RolUsuario.USUARIO;
            case "veterinario" -> RolUsuario.VETERINARIA;
            default -> throw new IllegalArgumentException("Rol inválido");
        };
    }
}
