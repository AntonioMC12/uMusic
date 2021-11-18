package es.antoniomc.uMusic;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBCancionDAO;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBListaReproduccionDAO;
import es.antoniomc.uMusic.model.Cancion;
import es.antoniomc.uMusic.model.ListaReproduccion;
import es.antoniomc.uMusic.model.Usuario;
import es.antoniomc.uMusic.model.usuarioHolder;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class buscarListaModalController {

	@FXML
	private Button btnRemove;

	@FXML
	private Button btnSuscribirse;

	@FXML
	private TableColumn<Cancion, String> colDisco;

	@FXML
	private TableColumn<Cancion, String> colDuracion;

	@FXML
	private TableColumn<Cancion, String> colGenero;

	@FXML
	private TableColumn<Cancion, String> colNombre;

	@FXML
	private TableColumn<ListaReproduccion, String> colPlaylist;

	@FXML
	private TableView<Cancion> tabCanciones;

	@FXML
	private TableView<ListaReproduccion> tabPlaylist;

	protected static ObservableList<ListaReproduccion> listas = FXCollections.observableArrayList();
	protected static ObservableList<Cancion> canciones = FXCollections.observableArrayList();

	@FXML
	public void initialize() {

		this.btnSuscribirse.setDisable(true);
		setInfo();

	}

	private void setInfo() {
		setListas();

	}

	/**
	 * Seleto la informacion en las listas
	 */
	private void setListas() {
		MariaDBListaReproduccionDAO listaDAO = new MariaDBListaReproduccionDAO(MariaDBConexion.getConexion());
		try {
			listas.setAll(listaDAO.mostrarTodos());
			this.colPlaylist.setCellValueFactory(listas -> {
				SimpleStringProperty v = new SimpleStringProperty();
				v.setValue(listas.getValue().getNombre());
				return v;
			});
			;
			this.tabPlaylist.setItems(listas);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Seteo las canciones de cada lista
	 * @param event
	 */
	@FXML
	void setCanciones(MouseEvent event) {
		usuarioHolder holder = usuarioHolder.getInstance();
		MariaDBCancionDAO cancionDAO = new MariaDBCancionDAO(MariaDBConexion.getConexion());
		if (holder.getUser() != null) {
			@SuppressWarnings("rawtypes")
			TablePosition pos = tabPlaylist.getSelectionModel().getSelectedCells().get(0);
			int row = pos.getRow();
			ListaReproduccion playlist = tabPlaylist.getItems().get(row);
			btnSuscribeState(playlist.getUsuario());
			try {
				canciones.setAll(cancionDAO.mostrarTodasEnPlaylist(playlist));

				this.colNombre.setCellValueFactory(canciones -> {
					SimpleStringProperty v = new SimpleStringProperty();
					v.setValue(canciones.getValue().getNombre());
					return v;
				});
				;
				this.colGenero.setCellValueFactory(canciones -> {
					SimpleStringProperty v = new SimpleStringProperty();
					v.setValue(canciones.getValue().getGenero().getNombre());
					return v;
				});
				;
				this.colDisco.setCellValueFactory(canciones -> {
					SimpleStringProperty v = new SimpleStringProperty();
					v.setValue(canciones.getValue().getDisco().getNombre());
					return v;
				});
				;
				this.colDuracion.setCellValueFactory(canciones -> {
					SimpleStringProperty v = new SimpleStringProperty();
					v.setValue(Float.toString(canciones.getValue().getDuracion()));
					return v;
				});
				;
				this.tabCanciones.setItems(canciones);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Método que habilita o desabilita el botón para desuscribirse de una playlist
	 * si el creador es uno mismo, no puede desuscribirse, pero si el creador es
	 * otro, si podría.
	 * 
	 * Si el usuario es otro, habilito el botón.
	 */
	private void btnSuscribeState(Usuario usuario) {
		if (!usuario.equals(usuarioHolder.getInstance().getUser())) {
			this.btnSuscribirse.setDisable(false);
		} else {
			this.btnSuscribirse.setDisable(true);
		}
	}

	/**
	 * Nos habilita el boton de eliminar cuando hemos seleccionado una canción
	 * 
	 * @param event
	 */
	@FXML
	private void btnRemoveState(MouseEvent event) {
		this.btnRemove.setDisable(false);
	}

	/**
	 * Método para suscribirse a una playlist, solo estará habilitado si la playlist
	 * no es del usuario.
	 */
	public void suscribirse() {
		@SuppressWarnings("rawtypes")
		// obtengo la playlist seleccionada.
		TablePosition pos = tabPlaylist.getSelectionModel().getSelectedCells().get(0);
		int row = pos.getRow();
		ListaReproduccion playlist = tabPlaylist.getItems().get(row);
		if (!PrimaryController.listas.contains(playlist)) {
			if (playlist != null) {
				MariaDBListaReproduccionDAO listaDAO = new MariaDBListaReproduccionDAO(MariaDBConexion.getConexion());
				try {
					listaDAO.suscribe(usuarioHolder.getInstance().getUser(), playlist);
					PrimaryController.listas.add(playlist);
					showValid("Suscrito con éxito");
				} catch (DAOException e) {
					showError("Error al suscribirse");
					e.printStackTrace();
				}
			} else {
				showError("Seleccione una playlist");
			}
		} else {
			showError("Ya estás suscrito");
		}
	}

	/**
	 * Elimina una canción de una playlist, se accede a ella cuando el botón está
	 * habilitado cuando ambos campos están seleccionados.
	 */
	public void eliminarCancion() {
		TablePosition pos = tabPlaylist.getSelectionModel().getSelectedCells().get(0);
		int row = pos.getRow();
		ListaReproduccion playlist = tabPlaylist.getItems().get(row);
		TablePosition pos2 = tabCanciones.getSelectionModel().getSelectedCells().get(0);
		int row2 = pos2.getRow();
		Cancion cancion = tabCanciones.getItems().get(row2);

		MariaDBCancionDAO cancionDAO = new MariaDBCancionDAO(MariaDBConexion.getConexion());
		try {
			cancionDAO.quitarDeLista(cancion, playlist);
			showValid("Eliminada con éxito");
		} catch (DAOException e) {
			showError("No se ha podido eliminar");
			e.printStackTrace();
		}
		canciones.remove(row2);
	}

	@FXML
	public void showError(String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setTitle("Error");
		alert.setContentText(text);
		alert.showAndWait();
	}

	@FXML
	public void showValid(String text) {
		Alert alert = new Alert(AlertType.INFORMATION);

		alert.setHeaderText(null);
		alert.setTitle("Éxito");
		alert.setContentText(text);
		alert.showAndWait();
	}

}
