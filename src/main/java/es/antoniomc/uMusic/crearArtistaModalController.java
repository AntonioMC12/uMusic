package es.antoniomc.uMusic;

import java.io.File;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBArtistaDAO;
import es.antoniomc.uMusic.model.Artista;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class crearArtistaModalController {
	
    @FXML
    private Button btnCrear;

    @FXML
    private Button btnAddImg;

    @FXML
    private ImageView imgFoto;

    @FXML
    private TextField txtNacionalidad;

    @FXML
    private TextField txtNombre;
    
    @SuppressWarnings("unused")
	private String fotoURL = "@media\\avatar-de-hombre.png";
    
    private boolean checkFields() {
    	return (!this.txtNombre.getText().trim().isEmpty() && !this.txtNacionalidad.getText().trim().isEmpty());
    }
    
    @FXML
    public void crearArtista() {
    	if(checkFields()) {
    		MariaDBArtistaDAO artistaDAO = new MariaDBArtistaDAO(MariaDBConexion.getConexion());
    		try {
				artistaDAO.guardar(new Artista(-1L,this.txtNombre.getText(),this.txtNacionalidad.getText(),this.fotoURL));
				showValid("Creado con éxito");
				clearFields();
			} catch (DAOException e) {
				showError("Error al crear");
				e.printStackTrace();
			}
    	}else {
    		showError("Rellene todos los campos");
    	}
    }
	
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
	
	private void clearFields() {
		this.txtNombre.clear();
		this.txtNacionalidad.clear();
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
