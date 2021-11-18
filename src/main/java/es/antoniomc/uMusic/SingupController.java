package es.antoniomc.uMusic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBUsuarioDAO;
import es.antoniomc.uMusic.model.Usuario;
import es.antoniomc.uMusic.model.usuarioHolder;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SingupController {

	@FXML
	private Button btnFoto;

	@FXML
	private Button btnLogin;

	@FXML
	private Button btnSingup;

	@FXML
	private TextField txtCorreo;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private PasswordField txtPassword2;

	@FXML
	private TextField txtUsuario;

	@FXML
	private ImageView imgFoto;

	private String fotoURL = "@media\\avatar-de-hombre.png";

	public void Singup(Event event) throws IOException {
		usuarioHolder holder = usuarioHolder.getInstance();
		String nombre = this.txtUsuario.getText();
		String password = this.txtPassword.getText();
		String correo = this.txtCorreo.getText();
		String foto = this.fotoURL;

		if (checkFields(nombre, password)) {

			MariaDBUsuarioDAO usuarioDAO = new MariaDBUsuarioDAO(MariaDBConexion.getConexion());
			List<String> nombres = new ArrayList<>();
			
			try {
				
				nombres = usuarioDAO.mostrarNombres();
				if (!nombres.contains(nombre)) {

					Usuario newuser = new Usuario(-1L, nombre, correo, foto, password);
					usuarioDAO.guardar(newuser);
					holder.setUser(usuarioDAO.mostrar(nombre));
					showValid("¡Usuario creado con éxito!");
					App.GoTo(event, "primary");
				} else {
					showError("El nombre introducido ya existe");
					this.txtUsuario.clear();
					this.txtPassword.clear();
					this.txtPassword2.clear();
					this.txtCorreo.getText();
				}
			} catch (DAOException e) {
				e.printStackTrace();
			}
		} else {
			showError("Has introducido mal algun dato");
			this.txtUsuario.clear();
			this.txtPassword.clear();
			this.txtPassword2.clear();
			this.txtCorreo.getText();
		}
	}

	@FXML
	public void setFoto() {
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
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setTitle("Información");
				alert.setContentText("Formato incorrecto: Debe elegir un tipo de archivo jpg o png.");
				alert.showAndWait();
			}
		} catch (Exception e) {
			// TODO: handle exception;
		}
	}

	public void GoLogin(Event event) {
		App.GoTo(event, "login");
		
	}

	public boolean checkFields(String nombre, String password) {
		return (!this.txtUsuario.getText().trim().isEmpty() && !this.txtPassword.getText().trim().isEmpty()
				&& !this.txtPassword2.getText().trim().isEmpty() && !this.txtCorreo.getText().trim().isEmpty()
				&& this.txtPassword.getText().equals(this.txtPassword2.getText()));
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
		this.txtUsuario.clear();
		this.txtPassword.clear();
		this.txtCorreo.clear();
		this.txtPassword2.clear();
		alert.setHeaderText(null);
		alert.setTitle("Error");
		alert.setContentText(text);
		alert.showAndWait();
	}

}
