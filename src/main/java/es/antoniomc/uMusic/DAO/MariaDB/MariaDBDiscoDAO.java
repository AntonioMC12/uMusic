package es.antoniomc.uMusic.DAO.MariaDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.DiscoDAO;
import es.antoniomc.uMusic.model.Artista;
import es.antoniomc.uMusic.model.Disco;

public class MariaDBDiscoDAO implements DiscoDAO {

	final String INSERT = "INSERT INTO disco(nombre, foto, fecha, id_artista) VALUES (?, ?, ?, ?)";
	final String UPDATE = "UPDATE disco SET nombre=?, foto = ?, fecha = ?, id_artista = ? WHERE id = ?";
	final String DELETE = "DELETE FROM disco WHERE id = ?";
	final String GETALL = "SELECT id, nombre, foto, fecha, id_artista FROM disco";
	final String GETONE = "SELECT id, nombre, foto, fecha, id_artista FROM disco WHERE id = ?";

	private Connection conn;

	public MariaDBDiscoDAO(Connection conn) {
		super();
		this.conn = conn;
	}

	@Override
	public void guardar(Disco a) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

			st.setString(1, a.getNombre());
			st.setString(2, a.getFoto());
			st.setDate(3, Date.valueOf(a.getFecha().toString()));
			st.setLong(4, a.getArtista().getId());

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
	public void editar(Disco a) throws DAOException {
		PreparedStatement st = null;
		try {

			st = conn.prepareStatement(UPDATE);
			st.setString(1, a.getNombre());
			st.setString(2, a.getFoto());
			st.setDate(3, Date.valueOf(a.getFecha().toString()));
			st.setLong(4, a.getArtista().getId());
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
	public void borrar(Disco a) throws DAOException {
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
	public List<Disco> mostrarTodos() throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Disco> discos = new ArrayList<>();

		try {

			st = conn.prepareStatement(GETALL);
			rs = st.executeQuery();

			while (rs.next()) {
				discos.add(convertir(rs));
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
		return discos;
	}

	@Override
	public Disco mostrar(Long id) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		Disco dummy = null;

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
	
	private Disco convertir(ResultSet rs) throws SQLException, DAOException {

		Long id = rs.getLong("id");
		String nombre = rs.getString("nombre");
		String foto = rs.getString("foto");
		LocalDate fecha = rs.getDate("fecha").toLocalDate();
		Artista artista = new MariaDBArtistaDAO(conn).mostrar(rs.getLong("id_artista"));
		Disco disco = new Disco(id,nombre,foto,fecha,artista);

		return disco;
	}

}
