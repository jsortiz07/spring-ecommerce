package com.johanapp.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.johanapp.ecommerce.model.DetalleOrden;
import com.johanapp.ecommerce.model.Orden;
import com.johanapp.ecommerce.model.Producto;
import com.johanapp.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ProductoService productoservice;
	
	//Para almacenar los dellates de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	// almacena los datos de la orden
	Orden orden = new Orden();
	
	@GetMapping("")
	public String home(Model model) {
		
		model.addAttribute("Productos",productoservice.findAll());
		return "usuario/home";
	}
	
	// ver detalles del producto
	@GetMapping("productohome/{id}") //gracias a pathvariable nos pasa el parametro id al getmapping
	public String productoHome(@PathVariable Integer id, Model model) { // el objeto model lleva informacion del backend hasta la vista
		log.info("Id enviado como parametro {}",id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoservice.get(id);
		producto = productoOptional.get(); // trae toda la data de producto
		
		model.addAttribute("producto",producto); // enviar datos desde la db a la vista
		return "usuario/productohome";
	}
	
	// agregar producto al carrito
	
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetalleOrden detallerOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal=0;
		Optional<Producto> optionalProducto = productoservice.get(id) ;
		log.info("Producto añadido: {}",optionalProducto.get());
		log.info("Cantidad {}", cantidad);
		producto = optionalProducto.get();
		
		detallerOrden.setCantidad(cantidad);
		detallerOrden.setPrecio(producto.getPrecio());
		detallerOrden.setNombre(producto.getNombre());
		detallerOrden.setTotal(producto.getPrecio()*cantidad);
		detallerOrden.setProducto(producto);// este es para capturar la llave foranea
		
		// validar que el producto no se añada 2 veces
		// Stream() Este nos permite realizar operaciones sobre la colección, como por ejemplo buscar, filtrar, reordenar, reducir, etc…
		
		
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
		
		if(!ingresado) {
			detalles.add(detallerOrden);
		}
			
		
		sumaTotal = detalles.stream().mapToDouble(dt ->dt.getTotal()).sum(); // suma el total de los productos que estan en el array list
		orden.setTotal(sumaTotal);
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		return "usuario/carrito";
	}
	
	//remover producto del carrito
	
	@GetMapping("/delete/cart/{id}")
	public String RemoveProductCart(@PathVariable Integer id, Model model) {
		
		//lista nueva de productos
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();
		
		for(DetalleOrden detalleOrden: detalles) {
			if(detalleOrden.getProducto().getId() !=id) {
				ordenesNueva.add(detalleOrden);
			}
		}
		
		// Poner la nueva lista con los productos restantes
		detalles=ordenesNueva;
		
		double sumaTotal = 0;
		
		sumaTotal = detalles.stream().mapToDouble(dt ->dt.getTotal()).sum(); // suma el total de los productos que estan en el array list
		orden.setTotal(sumaTotal);
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		return "usuario/carrito";
		
		//prueba commmit desde pc 2
	}
	
	@GetMapping("/getCart")
	public String getCart(Model model) {
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		
		return "/usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order() {
		
		return "usuario/resumenorden";
	}
	
}
