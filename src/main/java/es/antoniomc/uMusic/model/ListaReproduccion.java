package es.antoniomc.uMusic.model;

import java.util.List;
import java.util.Objects;

public class ListaReproduccion {

	private Long id;
	private String nombre;
	private String descripcion;
	private Usuario usuario;
	private List<Cancion> canciones;

	public ListaReproduccion(Long id, String nombre, String descripcion, Usuario usuario) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.usuario = usuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Cancion> getCanciones() {
		return canciones;
	}

	public void setCanciones(List<Cancion> canciones) {
		this.canciones = canciones;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListaReproduccion other = (ListaReproduccion) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return nombre;
	}

}
