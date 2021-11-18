package es.antoniomc.uMusic.model;

import java.time.LocalDate;
import java.util.List;

public class Disco {

	private Long id;
	private String nombre;
	private String foto;
	private LocalDate fecha;
	private Artista artista;
	private List<Cancion> canciones;

	public Disco(Long id, String nombre, String foto, LocalDate fecha, Artista artista) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.foto = foto;
		this.fecha = fecha;
		this.artista = artista;
	}

	public Long getId() {
		return id;
	}

	public void setId(long l) {
		this.id = l;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Artista getArtista() {
		return artista;
	}

	public void setArtista(Artista artista) {
		this.artista = artista;
	}

	public List<Cancion> getCanciones() {
		return canciones;
	}

	public void setCanciones(List<Cancion> canciones) {
		this.canciones = canciones;
	}

	@Override
	public String toString() {
		return nombre;
	}

}
