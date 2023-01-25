package com.johanapp.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.johanapp.ecommerce.model.Orden;
import com.johanapp.ecommerce.model.Usuario;
import com.johanapp.ecommerce.service.IOrdenService;
import com.johanapp.ecommerce.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	
	
	private final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired
	private IUsuarioService usuarioservice;
	
	@Autowired
	private IOrdenService ordenservice;
	
	
	//  /usuario/registro
	@GetMapping("/registro")
	public String create() {
		
		return "usuario/registro";
	}
	
	@PostMapping("/save")
	public String save(Usuario usuario) {
		LOGGER.info("Usuario registro: {}", usuario);
		usuario.setTipo("USER");
		usuarioservice.save(usuario);
		
		return "redirect:/";
	}
	
	@GetMapping("/login")
	
	public String login() {
		
		return "usuario/login";
	}
	
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		LOGGER.info("Accesos {}", usuario);
		
		Optional<Usuario> user = usuarioservice.findByEmail(usuario.getEmail());
		//LOGGER.info("Usuario de db: {}",user.get());

		//.out.println("este log si lo detecta"+user.get().toString());
		
		if (user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId()); // la sesion se guarda en la variable idusuario y se puede utilizar en cualquier parte
			if (user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
				
			}else {
				return "redirect:/";
			}
		}else {
			LOGGER.info("Usuario no existe");
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/compras")
	public String obtenerCompras(Model model, HttpSession session) {
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		
		Usuario usuario = usuarioservice.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		List<Orden> ordenes = ordenservice.findByUsuario(usuario);
		
		model.addAttribute("ordenes",ordenes);
		
		return "usuario/compras";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
		
		LOGGER.info("ID DE LA ORDEN {}",id);
		
		Optional <Orden> orden = ordenservice.findById(id);
		model.addAttribute("detalles",orden.get().getDetalle());
		
		//session
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		
		return "usuario/detallecompra";
	}
	
}
