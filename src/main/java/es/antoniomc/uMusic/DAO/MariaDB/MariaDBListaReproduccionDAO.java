package es.antoniomc.uMusic.DAO.MariaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.ListaReproduccionDAO;
import es.antoniomc.uMusic.model.ListaReproduccion;
import es.antoniomc.uMusic.model.Usuario;

public class MariaDBListaReproduccionDAO implements ListaReproduccionDAO {

	final String INSERT = "INSERT INTO listareproduccion(nombre, descripcion, id_usuario) VALUES (?, ?, ?)";
	final String UPDATE = "UPDATE listareproduccion SET nombre=?, descripcion = ?, id_usuario = ? WHERE id = ?";
	final String DELETE = "DELETE FROM listareproduccion WHERE id = ?";
	final String GETALL = "SELECT id, nombre, descripcion, id_usuario FROM listareproduccion";
	final String GETONE = "SELECT id, nombre, descripcion, id_usuario FROM listareproduccion WHERE id = ?";
	final String GETALLFROMUSER = "SELECT id, nombre, descripcion, id_usuario FROM listareproduccion WHERE id_usuario = ?";
	final String GETALLSUSCRIBED = "SELECT id_listaReproduccion FROM lista_usuario WHERE id_usuario = ?";
	final String DESUSCRIBE = "DELETE FROM lista_usuario WHERE id_usuario = ? AND id_listaReproduccion = ?";
	final String SUSCRIBE = "INSERT INTO lista_usuario(id_usuario, id_listaReproduccion) VALUES (?, ?)";

	private Connection conn;

	public MariaDBListaReproduccionDAO(Connection conn) {
		super();
		this.conn = conn;
	}

	@Override
	public void guardar(ListaReproduccion a) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

			st.setString(1, a.getNombre());
			st.setString(2, a.getDescripcion());
			st.setLong(3, a.getUsuario().getId());

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
	public void editar(ListaReproduccion a) throws DAOException {
		PreparedStatement st = null;
		try {

			st = conn.prepareStatement(UPDATE);
			st.setString(1, a.getNombre());
			st.setString(2, a.getDescripcion());
			st.setLong(3, a.getUsuario().getId());
			st.setLong(4, a.getId());

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
	public void borrar(ListaReproduccion a) throws DAOException {
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
	public List<ListaReproduccion> mostrarTodos() throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<ListaReproduccion> listas = new ArrayList<>();

		try {

			st = conn.prepareStatement(GETALL);
			rs = st.executeQuery();

			while (rs.next()) {
				listas.add(convertir(rs));
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
		return listas;
	}

	@Override
	public ListaReproduccion mostrar(Long id) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		ListaReproduccion dummy = null;

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

	public List<ListaReproduccion> mostrarCreadas(Usuario usuario) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<ListaReproduccion> listas = new ArrayList<>();

		try {

			st = conn.prepareStatement(GETALLFROMUSER);
			st.setLong(1, usuario.getId());
			rs = st.executeQuery();

			while (rs.next()) {
				listas.add(convertir(rs));
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
		return listas;
	}

	public List<ListaReproduccion> mostrarSuscritas(Usuario usuario) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<ListaReproduccion> listas = new ArrayList<>();

		try {

			st = conn.prepareStatement(GETALLSUSCRIBED);
			st.setLong(1, usuario.getId());
			rs = st.executeQuery();

			while (rs.next()) {
				listas.add(mostrar(rs.getLong("id_listaReproduccion")));
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
		return listas;
	}

	public void desuscribe(Usuario usuario, ListaReproduccion lista) throws DAOException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(DESUSCRIBE);
			st.setLong(1, usuario.getId());
			st.setLong(2, lista.getId());
			if (st.executeUpdate() == 0) {
				throw new DAOException("Puede que no se haya borrado bien");
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

	public void suscribe(Usuario usuario, ListaReproduccion lista) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(SUSCRIBE);

			st.setLong(1, usuario.getId());
			st.setLong(2, lista.getId());

			if (st.executeUpdate() == 0) {
				throw new DAOException("Puede que no se haya guardado");
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

	private ListaReproduccion convertir(ResultSet rs) throws SQLException, DAOException {

		Long id = rs.getLong("id");
		String nombre = rs.getString("nombre");
		String descripcion = rs.getString("descripcion");
		Usuario usuario = new MariaDBUsuarioDAO(conn).mostrar(rs.getLong("id_usuario"));
		ListaReproduccion lista = new ListaReproduccion(id, nombre, descripcion, usuario);

		return lista;
	}

}
