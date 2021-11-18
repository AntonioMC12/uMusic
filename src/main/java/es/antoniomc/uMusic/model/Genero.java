package es.antoniomc.uMusic.model;

public class Genero {
	
	private Long id;
	private String nombre;
	
	public Genero(Long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long l) {
		this.id = l;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return nombre;
	}
	
	
	

}
