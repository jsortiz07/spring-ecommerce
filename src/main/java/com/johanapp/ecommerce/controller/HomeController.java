package com.johanapp.ecommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.johanapp.ecommerce.model.Producto;
import com.johanapp.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ProductoService productoservice;
	
	@GetMapping("")
	public String home(Model model) {
		
		model.addAttribute("Productos",productoservice.findAll());
		return "usuario/home";
	}
	
	@GetMapping("productohome/{id}") //gracias a pathvariable nos pasa el parametro id al getmapping
	public String productoHome(@PathVariable Integer id, Model model) { // el objeto model lleva informacion del backend hasta la vista
		log.info("Id enviado como parametro {}",id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoservice.get(id);
		producto = productoOptional.get(); // trae toda la data de producto
		
		model.addAttribute("producto",producto); // enviar datos desde la db a la vista
		return "usuario/productohome";
	}
	
	@PostMapping("/cart")
	public String addCart() {
		
		return "usuario/carrito";
	}
}
