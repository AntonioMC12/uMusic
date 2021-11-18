package es.antoniomc.uMusic.DAO.MariaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.GeneroDAO;
import es.antoniomc.uMusic.model.Genero;

public class MariaDBGeneroDAO implements GeneroDAO {

	final String INSERT = "INSERT INTO genero(nombre) VALUES (?)";
	final String UPDATE = "UPDATE genero SET nombre=? WHERE id = ?";
	final String DELETE = "DELETE FROM genero WHERE id = ?";
	final String GETALL = "SELECT id, nombre FROM genero";
	final String GETONE = "SELECT id, nombre FROM genero WHERE id = ?";

	private Connection conn;

	public MariaDBGeneroDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void guardar(Genero a) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

			st.setString(1, a.getNombre());

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
	public void editar(Genero a) throws DAOException {
		PreparedStatement st = null;
		try {

			st = conn.prepareStatement(UPDATE);
			st.setString(1, a.getNombre());
			st.setLong(2, a.getId());
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
	public void borrar(Genero a) throws DAOException {
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
	public List<Genero> mostrarTodos() throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Genero> generos = new ArrayList<>();

		try {

			st = conn.prepareStatement(GETALL);
			rs = st.executeQuery();

			while (rs.next()) {
				generos.add(convertir(rs));
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
		return generos;
	}

	@Override
	public Genero mostrar(Long id) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		Genero dummy = null;

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

	private Genero convertir(ResultSet rs) throws SQLException {

		Long id = rs.getLong("id");
		String nombre = rs.getString("nombre");
		Genero genero = new Genero(id, nombre);

		return genero;
	}

}
