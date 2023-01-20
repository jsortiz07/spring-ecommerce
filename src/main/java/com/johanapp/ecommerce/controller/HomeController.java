package com.johanapp.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.johanapp.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private ProductoService productoservice;
	
	@GetMapping("")
	public String home(Model model) {
		
		model.addAttribute("Productos",productoservice.findAll());
		return "usuario/home";
	}
}
