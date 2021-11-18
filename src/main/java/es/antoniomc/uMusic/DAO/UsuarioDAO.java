package es.antoniomc.uMusic.DAO;

import java.util.List;

import es.antoniomc.uMusic.model.Usuario;

public interface UsuarioDAO extends IDAO<Usuario, Long> {
	
	public Usuario mostrar(String nombre) throws DAOException;
	
	public List<String> mostrarNombres() throws DAOException;

}
