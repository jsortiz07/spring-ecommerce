package com.johanapp.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.johanapp.ecommerce.model.Producto;
import com.johanapp.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/administrador")
public class AministradorController {
	
	@Autowired
	private ProductoService productoservice;
	
	@GetMapping("")
	public String home(Model model) {
		
		List<Producto> productos = productoservice.findAll();
		model.addAttribute("productos",productos);
		
		return "administrador/home";
	}
	
	

}
