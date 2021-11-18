package es.antoniomc.uMusic.model;

import java.util.List;

public class usuarioHolder {

	private Usuario usuario;
	private List<ListaReproduccion> listas;

	private final static usuarioHolder INSTANCE = new usuarioHolder();

	private usuarioHolder() {
	}

	public static usuarioHolder getInstance() {
		return INSTANCE;
	}

	public void setUser(Usuario usuario2) {
		this.usuario = usuario2;
	}

	public Usuario getUser() {
		return this.usuario;

	}

	public List<ListaReproduccion> getListas() {
		return listas;
	}

	public void setListas(List<ListaReproduccion> listas) {
		this.listas = listas;
	}

}