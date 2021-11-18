package es.antoniomc.uMusic.model;

public class Artista {
	
	private Long id;
	private String nombre;
	private String nacionalidad;
	private String foto;
	
	public Artista(Long id, String nombre, String nacionalidad, String foto) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.nacionalidad = nacionalidad;
		this.foto = foto;
	}
	

	public Artista() {
		this(-1L,"","","");
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

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}


	@Override
	public String toString() {
		return nombre;
	}
	
	
	
	

}
