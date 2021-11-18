package es.antoniomc.uMusic.DAO;

import java.util.List;

import es.antoniomc.uMusic.model.ListaReproduccion;
import es.antoniomc.uMusic.model.Usuario;

public interface ListaReproduccionDAO extends IDAO<ListaReproduccion, Long> {

	/**
	 * Método que devuelve todas las playlist que ha creado un usuario
	 * 
	 * @param usuario creador
	 * @return lista de listas de reproduccion
	 * @throws DAOException
	 */
	public List<ListaReproduccion> mostrarCreadas(Usuario usuario) throws DAOException;

	/**
	 * Método que devuelve todas las playlist que está suscrito un usuario
	 * 
	 * @param usuario suscriptor
	 * @return lista de listas de reproduccion suscritas
	 * @throws DAOException
	 */
	public List<ListaReproduccion> mostrarSuscritas(Usuario usuario) throws DAOException;

	/**
	 * Método que te desuscribe de una playlist
	 * 
	 * @param usuario 
	 * @param lista de la que te desuscriber
	 * @throws DAOException
	 */
	public void desuscribe(Usuario usuario, ListaReproduccion lista) throws DAOException;

	/**
	 * Método que te suscribe a una lista
	 * 
	 * @param usuario suscriptor
	 * @param lista lista que te suscribes
	 * @throws DAOException
	 */
	public void suscribe(Usuario usuario, ListaReproduccion lista) throws DAOException;

}
