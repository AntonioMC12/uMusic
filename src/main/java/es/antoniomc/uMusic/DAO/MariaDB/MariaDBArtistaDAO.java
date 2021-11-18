package es.antoniomc.uMusic.DAO.MariaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import es.antoniomc.uMusic.DAO.ArtistaDAO;
import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.model.Artista;

public class MariaDBArtistaDAO implements ArtistaDAO {

	final String INSERT = "INSERT INTO artista(nombre, nacionalidad, foto) VALUES (?, ?, ?)";
	final String UPDATE = "UPDATE artista SET nombre=?, nacionalidad = ?, foto = ? WHERE id = ?";
	final String DELETE = "DELETE FROM artista WHERE id = ?";
	final String GETALL = "SELECT id, nombre, nacionalidad, foto FROM artista";
	final String GETONE = "SELECT id, nombre, nacionalidad, foto FROM artista WHERE id = ?";

	private Connection conn;

	public MariaDBArtistaDAO(Connection conn) {
		super();
		this.conn = conn;
	}

	@Override
	public void guardar(Artista a) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

			st.setString(1, a.getNombre());
			st.setString(2, a.getNacionalidad());
			st.setString(3, a.getFoto());

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
	public void editar(Artista a) throws DAOException {
		PreparedStatement st = null;
		try {

			st = conn.prepareStatement(UPDATE);
			st.setString(1, a.getNombre());
			st.setString(2, a.getNacionalidad());
			st.setString(3, a.getFoto());
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
	public void borrar(Artista a) throws DAOException {
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
	public List<Artista> mostrarTodos() throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Artista> artistas = new ArrayList<>();

		try {

			st = conn.prepareStatement(GETALL);
			rs = st.executeQuery();

			while (rs.next()) {
				artistas.add(convertir(rs));
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
		return artistas;
	}

	@Override
	public Artista mostrar(Long id) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		Artista dummy = null;

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

	/**
	 * Convierte de un resultSet al objeto y lo devuelve.
	 * 
	 * @param rs ResulSet de la consulta
	 * @return Objeto obtenido 
	 * @throws SQLException Fallo al obtener algún campo
	 */
	private Artista convertir(ResultSet rs) throws SQLException {

		Long id = rs.getLong("id");
		String nombre = rs.getString("nombre");
		String nacionalidad = rs.getString("nacionalidad");
		String foto = rs.getString("foto");
		Artista artista = new Artista(id, nombre, nacionalidad, foto);

		return artista;
	}

}
