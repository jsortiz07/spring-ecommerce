package com.johanapp.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.johanapp.ecommerce.model.DetalleOrden;
import com.johanapp.ecommerce.model.Orden;
import com.johanapp.ecommerce.model.Producto;
import com.johanapp.ecommerce.model.Usuario;
import com.johanapp.ecommerce.service.IDetalleOrdenService;
import com.johanapp.ecommerce.service.IOrdenService;
import com.johanapp.ecommerce.service.IUsuarioService;
import com.johanapp.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ProductoService productoservice;
	
	@Autowired
	private IUsuarioService usuarioservice;
	
	@Autowired
	private IOrdenService ordenservice;
	
	@Autowired
	private IDetalleOrdenService detalleordenservice;
	
	
	//Para almacenar los dellates de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	// almacena los datos de la orden
	Orden orden = new Orden();
	
	@GetMapping("")
	public String home(Model model,HttpSession session) {
		log.info("sesion del usuario {}",session.getAttribute("idusuario"));
		
		model.addAttribute("productos",productoservice.findAll());
		
		// Obtener session
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		
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
	public String getCart(Model model,HttpSession session) {
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		
		//enviar sesion
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		
		return "/usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order(Model model, HttpSession session) {
		Usuario usuario = usuarioservice.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		model.addAttribute("usuario",usuario);
		
		return "usuario/resumenorden";
	}
	
	//guardar orden
	
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenservice.generarNumeroOrden());
		
		//usuario
		Usuario usuario = usuarioservice.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
		orden.setUsuario(usuario);
		ordenservice.save(orden);
		
		//guardar detalles
		for(DetalleOrden dt:detalles) {
			dt.setOrden(orden);
			detalleordenservice.save(dt);
			
		}
		//limpiar lista y orden
		orden =new Orden();
		detalles.clear();
		return "redirect:/";
		
	}
	
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model) {
		log.info("nombre del producto consultado: {}",nombre);
		List<Producto> productos =productoservice.findAll().stream().filter(p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
		model.addAttribute("productos", productos);
		return "usuario/home";
	}
	
	
	
}
