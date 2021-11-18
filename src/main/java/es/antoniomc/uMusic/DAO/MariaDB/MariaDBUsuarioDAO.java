package es.antoniomc.uMusic.DAO.MariaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.UsuarioDAO;
import es.antoniomc.uMusic.model.Usuario;
import es.antoniomc.uMusic.utils.encoder;

public class MariaDBUsuarioDAO implements UsuarioDAO {

	final String INSERT = "INSERT INTO usuario(nombre, correo, foto, password) VALUES (?, ?, ?, ?)";
	final String UPDATE = "UPDATE usuario SET nombre=?, correo = ?, foto = ?, password = ? WHERE id = ?";
	final String DELETE = "DELETE FROM usuario WHERE id = ?";
	final String GETALL = "SELECT id, nombre, correo, foto, password FROM usuario";
	final String GETONE = "SELECT id, nombre, correo, foto, password FROM usuario WHERE id = ?";
	final String GETBYNAME = "SELECT id, nombre, correo, foto, password FROM usuario WHERE nombre = ?";
	final String GETALLNAMES = "SELECT nombre from usuario";

	private Connection conn;

	public MariaDBUsuarioDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void guardar(Usuario a) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

			st.setString(1, a.getNombre());
			st.setString(2, a.getCorreo());
			st.setString(3, a.getFoto());
			st.setString(4, encoder.encrypt(a.getPassword()));

			if (st.executeUpdate() == 0) {
				throw new DAOException("Puede que no se haya guardado");
			}
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				a.setId(rs.getLong(1));
			} else {
				throw new DAOException("No se ha asignado ID");
			}

		} catch (SQLException e) {
			throw new DAOException("Error en guardar SQL", e);
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					throw new DAOException("Error al cerrar conexion", e2);
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					throw new DAOException("Error en SQL", e2);
				}
			}
		}

	}

	@Override
	public void editar(Usuario a) throws DAOException {
		PreparedStatement st = null;
		try {

			st = conn.prepareStatement(UPDATE);
			st.setString(1, a.getNombre());
			st.setString(2, a.getCorreo());
			st.setString(3, a.getFoto());
			st.setString(4, encoder.encrypt(a.getPassword()));
			st.setLong(5, a.getId());

			if (st.executeUpdate() == 0) {
				throw new DAOException("Puede que no se haya actualizado");
			}

		} catch (SQLException e) {
			throw new DAOException("Error de SQL", e);

		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e2) {
					throw new DAOException("Error al cerrar conexion", e2);
				}
			}
		}

	}

	@Override
	public void borrar(Usuario a) throws DAOException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(DELETE);
			st.setLong(1, a.getId());
			if (st.executeUpdate() == 0) {
				throw new DAOException("Puede que no se haya borrado bien");
			} else {
				a.setId(-1L);
			}
		} catch (SQLException e) {
			throw new DAOException("Error de SQL", e);
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e2) {
					throw new DAOException("Error al cerrar conexion", e2);
				}
			}
		}

	}

	@Override
	public List<Usuario> mostrarTodos() throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Usuario> usuarios = new ArrayList<>();

		try {

			st = conn.prepareStatement(GETALL);
			rs = st.executeQuery();

			while (rs.next()) {
				usuarios.add(convertir(rs));
			}

		} catch (SQLException e) {
			throw new DAOException("Error en SQL", e);

		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					throw new DAOException("Error en SQL", e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					throw new DAOException("Error en SQL", e2);
				}
			}
		}
		return usuarios;
	}

	@Override
	public Usuario mostrar(Long id) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		Usuario dummy = null;

		try {

			st = conn.prepareStatement(GETONE);
			st.setLong(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				dummy = convertir(rs);
			} else {
				throw new DAOException("No se ha encontrado ese registro");
			}

		} catch (SQLException e) {
			throw new DAOException("Error en SQL", e);

		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					throw new DAOException("Error en SQL", e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					throw new DAOException("Error en SQL", e2);
				}
			}
		}
		return dummy;
	}

	@Override
	public Usuario mostrar(String nombre) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		Usuario dummy = null;

		try {

			st = conn.prepareStatement(GETBYNAME);
			st.setString(1, nombre);
			rs = st.executeQuery();
			if (rs.next()) {
				dummy = convertir(rs);
			} else {
				throw new DAOException("No se ha encontrado ese registro");
			}

		} catch (SQLException e) {
			throw new DAOException("Error en SQL", e);

		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					throw new DAOException("Error en SQL", e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					throw new DAOException("Error en SQL", e2);
				}
			}
		}
		return dummy;
	}
	
	@Override
	public List<String> mostrarNombres() throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<String> usuarios = new ArrayList<>();

		try {

			st = conn.prepareStatement(GETALLNAMES);
			rs = st.executeQuery();

			while (rs.next()) {
				usuarios.add(rs.getString(1));
			}

		} catch (SQLException e) {
			throw new DAOException("Error en SQL", e);

		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					throw new DAOException("Error en SQL", e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					throw new DAOException("Error en SQL", e2);
				}
			}
		}
		return usuarios;
	}

	private Usuario convertir(ResultSet rs) throws SQLException, DAOException {

		Long id = rs.getLong("id");
		String nombre = rs.getString("nombre");
		String correo = rs.getString("correo");
		String foto = rs.getString("foto");
		String password = rs.getString("password");

		Usuario usuario = new Usuario(id, nombre, correo, foto, password);

		return usuario;
	}



}
