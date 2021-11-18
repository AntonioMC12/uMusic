package es.antoniomc.uMusic;

import java.io.File;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBArtistaDAO;
import es.antoniomc.uMusic.model.Artista;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class editarArtistaModalController {

	@FXML
	private Button btnAddImg;

	@FXML
	private Button btnGuardar;

	@FXML
	private ComboBox<Artista> cbArtistas;

	@FXML
	private ImageView imgFoto;

	@FXML
	private TextField txtNacionalidad;

	@FXML
	private TextField txtNombre;

	private String fotoURL = "@media\\avatar-de-hombre.png";
	protected static ObservableList<Artista> artistas = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		MariaDBArtistaDAO artistaDAO = new MariaDBArtistaDAO(MariaDBConexion.getConexion());
		try {
			artistas.setAll(artistaDAO.mostrarTodos());
			this.cbArtistas.setItems(artistas);
		} catch (DAOException e) {
			showError("Error al obtener los artistas");
			e.printStackTrace();
		}

	}

	/**
	 * Método que setea los campos con el artista seleccionado
	 */
	public void setCampos() {
		this.txtNombre.setText(this.cbArtistas.getValue().getNombre());
		this.txtNacionalidad.setText(this.cbArtistas.getValue().getNacionalidad());
		setFoto(this.cbArtistas.getValue().getFoto());
	}

	/**
	 * Método que guarda los artistas una vez editados
	 * @param event
	 */
	@FXML
	void guardarArtista(ActionEvent event) {
		if (checkFields()) {
			MariaDBArtistaDAO artistaDAO = new MariaDBArtistaDAO(MariaDBConexion.getConexion());
			try {
				artistaDAO.editar(new Artista(this.cbArtistas.getValue().getId(), this.txtNombre.getText(),
						this.txtNacionalidad.getText(), this.fotoURL));
				showValid("Editado con éxito");
			} catch (DAOException e) {
				showError("Error al editar");
				e.printStackTrace();
			}
		}else {
			showError("Debe rellenar todos los campos");
		}
	}

	private boolean checkFields() {
		return (!this.txtNombre.getText().trim().isEmpty() && !this.txtNacionalidad.getText().trim().isEmpty());
	}

	@FXML
	void setFoto(ActionEvent event) {
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
