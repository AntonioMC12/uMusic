package es.antoniomc.uMusic.model;

import java.util.Objects;

public class Cancion {

	private Long id;
	private String nombre;
	private float duracion;
	private Disco disco;
	private Genero genero;
	private int reproducciones;

	public Cancion(Long id, String nombre, float duracion, Disco disco, Genero genero, int reproducciones) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.duracion = duracion;
		this.disco = disco;
		this.genero = genero;
		this.reproducciones = reproducciones;
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

	public float getDuracion() {
		return duracion;
	}

	public void setDuracion(float duracion) {
		this.duracion = duracion;
	}

	public Disco getDisco() {
		return disco;
	}

	public void setDisco(Disco disco) {
		this.disco = disco;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public int getReproducciones() {
		return reproducciones;
	}

	public void setReproducciones(int reproducciones) {
		this.reproducciones = reproducciones;
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
		Cancion other = (Cancion) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return nombre;
	}

}
