package es.antoniomc.uMusic;

import java.io.File;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBArtistaDAO;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBDiscoDAO;
import es.antoniomc.uMusic.model.Artista;
import es.antoniomc.uMusic.model.Disco;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class editarDiscoModalController {

	@FXML
	private Button btnAddImg;

	@FXML
	private Button btnGuardar;

	@FXML
	private ComboBox<Artista> cbArtista;

	@FXML
	private ComboBox<Disco> cbDisco;

	@FXML
	private DatePicker fchFecha;

	@FXML
	private ImageView imgFoto;

	@FXML
	private TextField txtNombre;

	private String fotoURL = "@media\\avatar-de-hombre.png";

	protected static ObservableList<Artista> artistas = FXCollections.observableArrayList();
	protected static ObservableList<Disco> discos = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		MariaDBArtistaDAO artistaDAO = new MariaDBArtistaDAO(MariaDBConexion.getConexion());
		MariaDBDiscoDAO discoDAO = new MariaDBDiscoDAO(MariaDBConexion.getConexion());
		try {
			artistas.setAll(artistaDAO.mostrarTodos());
			discos.setAll(discoDAO.mostrarTodos());
			this.cbDisco.setItems(discos);
			this.cbArtista.setItems(artistas);
		} catch (DAOException e) {
			showError("Error al obtener los artistas");
			e.printStackTrace();
		}

	}

	@FXML
	void editarDisco(ActionEvent event) {
		if (checkFields()) {
			MariaDBDiscoDAO discoDAO = new MariaDBDiscoDAO(MariaDBConexion.getConexion());
			try {
				discoDAO.editar(new Disco(this.cbDisco.getValue().getId(), this.txtNombre.getText(), fotoURL, this.fchFecha.getValue(),
						this.cbArtista.getValue()));
				showValid("Editado con éxito");

			} catch (DAOException e) {
				showError("Error al crear");
				e.printStackTrace();
			}
		} else {
			showError("Rellene todos los campos");
		}
	}

	/**
	 * Método para setear la foto introducida por el buscador
	 * 
	 * @param event
	 */
	@FXML
	public void setFoto(ActionEvent event) {
		File file = null;
		FileChooser filechooser = new FileChooser();
		filechooser.setTitle("Selecionar imagen...");
		try {
			file = filechooser.showOpenDialog(null);
			if (file != null && file.getPath().matches(".+\\.png") || file.getPath().matches(".+\\.jpg")) {
				Image img = new Image("file:\\" + file.getPath());
				imgFoto.setImage(img);
				this.fotoURL = file.getPath();
			} else {
				// extension incorrecta
				showError("Extension incorrecta, asegurese de que sea PNG o JPG");
			}
		} catch (Exception e) {
			// TODO: handle exception;
		}
	}

	private boolean checkFields() {
		return (!this.txtNombre.getText().trim().isEmpty() && !fchFecha.getValue().toString().trim().isEmpty()
				&& !this.cbArtista.getSelectionModel().isEmpty());
	}
	
	public void setCampos() {
		this.txtNombre.setText(this.cbDisco.getValue().getNombre());
		this.fchFecha.setValue(this.cbDisco.getValue().getFecha());
		this.cbArtista.setValue(this.cbDisco.getValue().getArtista());
		setFoto(this.cbDisco.getValue().getFoto());
	}
	
	@FXML
	void setFoto(String url) {
		Image img = new Image("file:\\" + url);
		imgFoto.setImage(img);
	}

	@FXML
	public void showValid(String text) {
		Alert alert = new Alert(AlertType.INFORMATION);

		alert.setHeaderText(null);
		alert.setTitle("Éxito");
		alert.setContentText(text);
		alert.showAndWait();
	}

	@FXML
	public void showError(String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setTitle("Error");
		alert.setContentText(text);
		alert.showAndWait();
	}

}
