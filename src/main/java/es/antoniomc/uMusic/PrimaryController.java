package es.antoniomc.uMusic;

import java.io.File;
import java.io.IOException;

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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrimaryController {

	@FXML
	private Button btnBorrar;

	@FXML
	private Button btnCrear;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnSalir;

	@FXML
	private Button btnDesuscribirse;

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
	private ImageView imgFoto;

	@FXML
	private MenuItem menuArtistas;

	@FXML
	private MenuItem menuCanciones;

	@FXML
	private MenuItem menuDiscos;

	@FXML
	private MenuItem menuPlaylist;

	@FXML
	private TableView<Cancion> tabCanciones;

	@FXML
	private TableView<ListaReproduccion> tabPlaylist;

	@FXML
	private Text txtUsuario;

	protected static ObservableList<ListaReproduccion> listas = FXCollections.observableArrayList();
	protected static ObservableList<Cancion> canciones = FXCollections.observableArrayList();

	@FXML
	public void initialize() {

		this.btnDesuscribirse.setDisable(true);
		setInfo();

	}

	/**
	 * Seteo la imagen de perfil
	 * @param url
	 */
	private void setImg(String url) {
		File file = new File(url);
		Image image = new Image(file.toURI().toString());
		this.imgFoto.setImage(image);
	}

	/**
	 * Método que me devuelve al login
	 * @param event
	 */
	@FXML
	public void GoToLogin(Event event) {
		App.GoTo(event, "login");
	}

	/**
	 * Seteo la info del usuario
	 */
	private void setInfo() {
		usuarioHolder holder = usuarioHolder.getInstance();
		if (holder.getUser() != null) {
			this.txtUsuario.setText(holder.getUser().getNombre());
			setImg(holder.getUser().getFoto());
		}

		setListas(holder);

	}

	/**
	 * Método para setear las listas del usuario, tanto las listas propias como las
	 * listas que está suscrito el usuario.
	 * 
	 * @param holder Singleton para almecenar el usuario actual y sus listas.
	 */
	private void setListas(usuarioHolder holder) {
		MariaDBListaReproduccionDAO listaDAO = new MariaDBListaReproduccionDAO(MariaDBConexion.getConexion());
		try {
			listas.setAll(listaDAO.mostrarCreadas(holder.getUser()));
			listaDAO.mostrarSuscritas(holder.getUser()).forEach(lista -> listas.add(lista));
			holder.setListas(listas);
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
	 * Método que setea en la tableView las canciones de la playlist seleccionada
	 */
	public void setCanciones() {
		usuarioHolder holder = usuarioHolder.getInstance();
		MariaDBCancionDAO cancionDAO = new MariaDBCancionDAO(MariaDBConexion.getConexion());
		if (holder.getUser() != null) {
			@SuppressWarnings("rawtypes")
			// obtengo la playlist seleccionada
			TablePosition pos = tabPlaylist.getSelectionModel().getSelectedCells().get(0);
			int row = pos.getRow();
			ListaReproduccion playlist = tabPlaylist.getItems().get(row);
			// compruebo si puedo habilitar el botón para desuscribirme
			btnState(playlist.getUsuario());
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
	 * Método para desuscribirse de una playlist, solo estará habilitado si la
	 * playlist no es del usuario.
	 */
	public void desuscribirse() {
		@SuppressWarnings("rawtypes")
		// obtengo la playlist seleccionada.
		TablePosition pos = tabPlaylist.getSelectionModel().getSelectedCells().get(0);
		int row = pos.getRow();
		ListaReproduccion playlist = tabPlaylist.getItems().get(row);
		if (playlist != null) {
			listas.remove(playlist);
			MariaDBListaReproduccionDAO listaDAO = new MariaDBListaReproduccionDAO(MariaDBConexion.getConexion());
			try {
				listaDAO.desuscribe(usuarioHolder.getInstance().getUser(), playlist);
				showValid("Desuscrito con éxito");
			} catch (DAOException e) {
				showError("Ha ocurrido un error");
				e.printStackTrace();
			}
		} else {
			showError("Seleccione una playlist");
		}

	}

	/**
	 * Método que habilita o desabilita el botón para desuscribirse de una playlist
	 * si el creador es uno mismo, no puede desuscribirse, pero si el creador es
	 * otro, si podría.
	 * 
	 * Si el usuario es otro, habilito el botón.
	 */
	private void btnState(Usuario usuario) {
		if (!usuario.equals(usuarioHolder.getInstance().getUser())) {
			this.btnDesuscribirse.setDisable(false);
		}else {
			this.btnDesuscribirse.setDisable(true);
		}
	}

	/**
	 * Método que abre una ventana modal
	 * 
	 * @param event
	 * @param url   nombre del fichero fxml
	 */
	private void openModal(ActionEvent event, String url) {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(url));
		Parent modal;
		try {
			modal = fxmlLoader.load();
			Stage modalStage = new Stage();
			modalStage.initModality(Modality.APPLICATION_MODAL);
			modalStage.initOwner(App.rootstage);
			Scene modalScene = new Scene(modal);
			modalStage.setScene(modalScene);
			modalStage.showAndWait();
			modalStage.setResizable(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void crearButton(ActionEvent event) {
		openModal(event, "crearModal.fxml");
	}

	@FXML
	public void editarButton(ActionEvent event) {
		openModal(event, "editarModal.fxml");
	}

	@FXML
	public void borrarButton(ActionEvent event) {
		openModal(event, "borrarModal.fxml");
	}

	@FXML
	public void menuCancion(ActionEvent event) {
		openModal(event, "buscarCancionesModal.fxml");
	}

	@FXML
	public void menuListas(ActionEvent event) {
		openModal(event, "buscarListaModal.fxml");
	}

	@FXML
	public void menuArtista(ActionEvent event) {
		openModal(event, "buscarArtistaModal.fxml");
	}

	@FXML
	public void menuDisco(ActionEvent event) {
		openModal(event, "buscarDiscoModal.fxml");
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
