package es.antoniomc.uMusic;

import java.io.IOException;
import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBUsuarioDAO;
import es.antoniomc.uMusic.model.Usuario;
import es.antoniomc.uMusic.model.usuarioHolder;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import es.antoniomc.uMusic.utils.encoder;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LoginController {

	@FXML
	private Button btnLogin;

	@FXML
	private Button btnSingup;

	@FXML
	private ImageView imgLogo;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private TextField txtUsuario;

	@FXML
	public void initialize() {
	}

	/**
	 * Método que comprueba los datos de los usuarios para el correcto logeo en la
	 * aplicación haciendo una consulta en la base de datos para comprar que existe
	 * y la veracidad de la información introducida.
	 * 
	 * @param event
	 */
	@FXML
	public void Login(ActionEvent event) throws IOException {
		System.out.println("Entro");
		String nombre = this.txtUsuario.getText();
		String pass = this.txtPassword.getText();
		usuarioHolder holder = usuarioHolder.getInstance();

		if (checkFields(nombre, pass)) {
			MariaDBUsuarioDAO usuario = new MariaDBUsuarioDAO(MariaDBConexion.getConexion());
			try {
				holder.setUser(usuario.mostrar(nombre));
				App.GoTo(event, "primary");
			} catch (DAOException e1) {
				showError("Error en la base de datos");
				e1.printStackTrace();
			}
		} else {
			showError("Has introducido mal algún dato");
		}
	}

	/**
	 * Método que comprueba que los datos del usuario que se va a logear estén bien
	 * introducidos y se encuentren en la base de datos y en tal caso comprobar la
	 * contraseña del mismo.
	 * 
	 * @param nombre Nombre del usuario a logear
	 * @param pass   Contraseña del usuario a logear encriptada
	 * @return True si el login se ha realizado correctamente.
	 */
	@FXML
	public boolean checkFields(String nombre, String pass) {
		boolean check = false;

		if (!this.txtUsuario.getText().trim().isEmpty() && !this.txtPassword.getText().trim().isEmpty()) {
			MariaDBUsuarioDAO usuarioDAO = new MariaDBUsuarioDAO(MariaDBConexion.getConexion());
			Usuario usuario = null;
			try {
				usuario = usuarioDAO.mostrar(nombre);
			} catch (DAOException e) {
				showError("Error, el usuario no existe.");
				e.printStackTrace();
			}
			if (usuario != null && usuario.getPassword() != null && usuario.getId() != -1) {
				check = encoder.matchPass(pass, usuario.getPassword());
			}
		}
		return check;
	}	

	public void GoSingup(Event event) {
		App.GoTo(event, "register");
	}
	
	@FXML
	public void showError(String text) {
		Alert alert = new Alert(AlertType.ERROR);
		this.txtUsuario.clear();
		this.txtPassword.clear();
		alert.setHeaderText(null);
		alert.setTitle("Error");
		alert.setContentText(text);
		alert.showAndWait();
	}

}
