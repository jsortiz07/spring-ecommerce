package com.johanapp.ecommerce.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.johanapp.ecommerce.model.Producto;
import com.johanapp.ecommerce.model.Usuario;
import com.johanapp.ecommerce.service.ProductoService;
import com.johanapp.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class); // se emplea para testear por
																						// consola

	@Autowired
	private ProductoService productoservice;
	@Autowired
	private UploadFileService upload;

	@GetMapping("")
	public String show(Model model) { // el objeto model lleva informacion del backend hasta la vista
		model.addAttribute("productos", productoservice.findAll());
		return "productos/show";
	}

	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}

	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		LOGGER.info("Este es el objeto producto {}", producto);
		Usuario u = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(u);

		// imagen
		if (producto.getId() == null) { // cuando se crea un producto
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		} else {

		}
		productoservice.save(producto);
		return "redirect:/productos";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalproducto = productoservice.get(id);
		producto = optionalproducto.get();

		LOGGER.info("Producto buscado {}", producto);
		model.addAttribute("producto", producto);
		return "productos/edit";
	}

	@PostMapping("/update")
	public String update(Producto producto,@RequestParam("img") MultipartFile file) throws IOException {
		
		Producto p = new Producto();
		p = productoservice.get(producto.getId()).get();
		
		if (file.isEmpty()) { // editamos el producto pero no cambiamos de img
			
			producto.setImagen(p.getImagen());
			
		} else { // cuando se edita la imagen
			

			// eliminar cuando no sea la imagen por defecto
			if (!p.getImagen().equals("default.jpg")) {
				upload.deleteImage(p.getImagen());

			}
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoservice.update(producto);
		return "redirect:/productos";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		Producto p = new Producto();
		p = productoservice.get(id).get();

		// eliminar cuando no sea la imagen por defecto
		if (!p.getImagen().equals("default.jpg")) {
			upload.deleteImage(p.getImagen());
		}

		productoservice.delete(id);

		return "redirect:/productos";

	}
}
