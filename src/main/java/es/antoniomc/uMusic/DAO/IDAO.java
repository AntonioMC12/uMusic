package es.antoniomc.uMusic.DAO;

import java.util.List;

public interface IDAO<T, K> {
	
	/**
	 * Método que guarda un objeto en la base de datos
	 * 
	 * @param a
	 * @throws DAOException
	 */
	void guardar(T a) throws DAOException;

	/**
	 * Método edita un objeto existente en la base de datos
	 * @param a
	 * @throws DAOException
	 */
	void editar(T a) throws DAOException;

	/**
	 * Método que borra un objeto existente en la base de datos
	 * @param a
	 * @throws DAOException
	 */
	void borrar(T a) throws DAOException;

	/**
	 * Método que muestra todos los objetos de una tabla
	 * @return
	 * @throws DAOException
	 */
	List<T> mostrarTodos() throws DAOException;
	
	/**
	 * Método que muestra un objeto por la id dada
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	T mostrar(K id) throws DAOException;

}
