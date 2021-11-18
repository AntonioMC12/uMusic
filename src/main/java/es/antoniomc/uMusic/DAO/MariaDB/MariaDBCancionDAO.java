package es.antoniomc.uMusic.DAO.MariaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import es.antoniomc.uMusic.DAO.CancionDAO;
import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.model.Cancion;
import es.antoniomc.uMusic.model.Disco;
import es.antoniomc.uMusic.model.Genero;
import es.antoniomc.uMusic.model.ListaReproduccion;

public class MariaDBCancionDAO implements CancionDAO {

	final String INSERT = "INSERT INTO cancion(nombre, duracion, id_disco, id_genero, reproducciones) VALUES (?, ?, ?, ?, ?)";
	final String UPDATE = "UPDATE cancion SET nombre=?, duracion = ?, id_disco = ?, id_genero = ?, reproducciones = ? WHERE id = ?";
	final String DELETE = "DELETE FROM cancion WHERE id = ?";
	final String GETALL = "SELECT id, nombre, duracion, id_disco, id_genero, reproducciones FROM cancion";
	final String GETONE = "SELECT id, nombre, duracion, id_disco, id_genero, reproducciones FROM cancion WHERE id = ?";
	final String GETALLFROMPLAYLIST = "SELECT id_cancion FROM cancion_lista WHERE id_listaReproduccion = ?";
	final String ADDTOPLAYLIST = "INSERT INTO cancion_lista(id_cancion,id_listaReproduccion) VALUES (?, ?)";
	final String REMOVEFROMPLAYLIST = "DELETE FROM cancion_lista WHERE id_cancion = ? AND id_listaReproduccion = ?";

	private Connection conn;

	public MariaDBCancionDAO(Connection conn) {
		super();
		this.conn = conn;
	}

	@Override
	public void guardar(Cancion a) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

			st.setString(1, a.getNombre());
			st.setFloat(2, a.getDuracion());
			st.setLong(3, a.getDisco().getId());
			st.setLong(4, a.getGenero().getId());
			st.setInt(5, a.getReproducciones());

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
	public void editar(Cancion a) throws DAOException {
		PreparedStatement st = null;
		try {

			st = conn.prepareStatement(UPDATE);
			st.setString(1, a.getNombre());
			st.setFloat(2, a.getDuracion());
			st.setLong(3, a.getDisco().getId());
			st.setLong(4, a.getGenero().getId());
			st.setInt(5, a.getReproducciones());
			st.setLong(6, a.getId());

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
	public void borrar(Cancion a) throws DAOException {
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
	public List<Cancion> mostrarTodos() throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Cancion> canciones = new ArrayList<>();

		try {

			st = conn.prepareStatement(GETALL);
			rs = st.executeQuery();

			while (rs.next()) {
				canciones.add(convertir(rs));
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
		return canciones;
	}

	@Override
	public Cancion mostrar(Long id) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		Cancion dummy = null;

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

	public List<Cancion> mostrarTodasEnPlaylist(ListaReproduccion lista) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Cancion> canciones = new ArrayList<>();

		try {

			st = conn.prepareStatement(GETALLFROMPLAYLIST);
			st.setLong(1, lista.getId());
			rs = st.executeQuery();

			while (rs.next()) {
				canciones.add(mostrar(rs.getLong("id_cancion")));
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
		return canciones;
	}

	public void añadirALista(Cancion cancion, ListaReproduccion lista) throws DAOException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(ADDTOPLAYLIST);

			st.setLong(1, cancion.getId());
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

	public void quitarDeLista(Cancion cancion, ListaReproduccion lista) throws DAOException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(REMOVEFROMPLAYLIST);
			st.setLong(1, cancion.getId());
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

	private Cancion convertir(ResultSet rs) throws SQLException, DAOException {

		Long id = rs.getLong("id");
		String nombre = rs.getString("nombre");
		float duracion = rs.getFloat("duracion");
		Disco disco = new MariaDBDiscoDAO(conn).mostrar(rs.getLong("id_disco"));
		Genero genero = new MariaDBGeneroDAO(conn).mostrar(rs.getLong("id_genero"));
		int reproducciones = rs.getInt("reproducciones");
		Cancion cancion = new Cancion(id, nombre, duracion, disco, genero, reproducciones);

		return cancion;
	}

}
