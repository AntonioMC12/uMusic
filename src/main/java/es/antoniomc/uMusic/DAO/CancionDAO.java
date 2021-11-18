package es.antoniomc.uMusic.DAO;

import java.util.List;

import es.antoniomc.uMusic.model.Cancion;
import es.antoniomc.uMusic.model.ListaReproduccion;

public interface CancionDAO extends IDAO<Cancion, Long> {

	/**
	 * Método que muestra todas las canciones que contine una playlist
	 * 
	 * @param lista a consultar
	 * @return lista de canciones
	 * @throws DAOException
	 */
	public List<Cancion> mostrarTodasEnPlaylist(ListaReproduccion lista) throws DAOException;

	/**
	 * Método que añade una cancion a una playlist
	 * 
	 * @param cancion a añadir
	 * @param lista en la que añadimos
	 * @throws DAOException
	 */
	public void añadirALista(Cancion cancion, ListaReproduccion lista) throws DAOException;

	/**
	 * Método que borra una canción de una playlist
	 * 
	 * @param cancion a borrar
	 * @param lista de la que borramos
	 * @throws DAOException
	 */
	public void quitarDeLista(Cancion cancion, ListaReproduccion lista) throws DAOException;
}
