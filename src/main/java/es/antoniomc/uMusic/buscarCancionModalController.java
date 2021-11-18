package es.antoniomc.uMusic;

import java.util.List;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBCancionDAO;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBListaReproduccionDAO;
import es.antoniomc.uMusic.model.Cancion;
import es.antoniomc.uMusic.model.ListaReproduccion;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

public class buscarCancionModalController {

	@FXML
	private Button btnAdd;

	@FXML
	private ComboBox<ListaReproduccion> cbPlaylist;

	@FXML
	private TableColumn<Cancion, String> colDisco;

	@FXML
	private TableColumn<Cancion, String> colDuracion;

	@FXML
	private TableColumn<Cancion, String> colGenero;

	@FXML
	private TableColumn<Cancion, String> colNombre;

	@FXML
	private TableView<Cancion> tabCanciones;

	protected static ObservableList<ListaReproduccion> listas = FXCollections.observableArrayList();
	protected static ObservableList<Cancion> canciones = FXCollections.observableArrayList();

	@FXML
	public void initialize() {

		this.btnAdd.setDisable(true);
		setInfo();
		this.cbPlaylist.setItems(listas);

	}

	/**
	 * Seteo las listas en las tablas
	 */
	private void setInfo() {
		setCanciones();
		MariaDBListaReproduccionDAO listasDAO = new MariaDBListaReproduccionDAO(MariaDBConexion.getConexion());
		try {
			listas.setAll(listasDAO.mostrarTodos());
		} catch (DAOException e) {
			showError("Error al cargar las playlist");
			e.printStackTrace();
		}

	}

	/**
	 * Seteo las canciones de cada lista al pulsar sobre ella
	 */
	public void setCanciones() {
		MariaDBCancionDAO cancionDAO = new MariaDBCancionDAO(MariaDBConexion.getConexion());
		this.btnAdd.setDisable(false);
		try {
			canciones.setAll(cancionDAO.mostrarTodos());

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

	/**
	 * Función que se ejecuta cuando pulsas el botón de añadir, comprueba que la
	 * cancion no está en la lista y llama a la funcion para añadir dicha canción a
	 * la playlist.
	 */
	public void btnAdd() {
		// primero checkeo que tenga los datos bien seleccionados
		if (checkFields()) {
			@SuppressWarnings("rawtypes")
			TablePosition pos = tabCanciones.getSelectionModel().getSelectedCells().get(0);
			int row = pos.getRow();
			Cancion cancion = tabCanciones.getItems().get(row);
			// ahora obtengo todas las canciones de dicha playlist para que no añadamos
			// una ya existente
			MariaDBCancionDAO cancionDAO = new MariaDBCancionDAO(MariaDBConexion.getConexion());
			try {
				List<Cancion> canciones = cancionDAO.mostrarTodasEnPlaylist(this.cbPlaylist.getValue());
				if (!canciones.contains(cancion)) {
					addToPlaylist(cancion, this.cbPlaylist.getValue());
				} else {
					showError("La cancion ya está en la playlist");
				}
			} catch (DAOException e) {
				showError("Error al acceder a la playlist");
				e.printStackTrace();
			}
		} else {
			showError("Selecciona bien los datos");
		}
	}

	/**
	 * Método para comprobar que tengo bien seleccionados los campos para añadir una
	 * cancion
	 */
	private boolean checkFields() {
		boolean checkFields = false;
		// obtengo la playlist seleccionada.
		@SuppressWarnings("rawtypes")
		TablePosition pos = tabCanciones.getSelectionModel().getSelectedCells().get(0);
		int row = pos.getRow();
		Cancion cancion = tabCanciones.getItems().get(row);
		if (this.cbPlaylist.getValue() != null && cancion != null) {
			checkFields = true;
		}

		return checkFields;

	}

	/**
	 * Funcion que añade una cancion a una playlist
	 * 
	 * @param cancion Cancion a añadir
	 * @param lista   Lista a la que se añade
	 */
	public void addToPlaylist(Cancion cancion, ListaReproduccion lista) {
		if (cancion != null && lista != null) {
			MariaDBCancionDAO cancionDAO = new MariaDBCancionDAO(MariaDBConexion.getConexion());
			try {
				cancionDAO.añadirALista(cancion, lista);
				showValid("Añadida con éxito");
			} catch (DAOException e) {
				showError("Fallo al añadir");
				e.printStackTrace();
			}
		}
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
