package es.antoniomc.uMusic;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBGeneroDAO;
import es.antoniomc.uMusic.model.Genero;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class crearGeneroModalController {

	@FXML
	private Button btnCrear;

	@FXML
	private TextField txtNombre;

	@FXML
	void crearGenero(ActionEvent event) {
		if (checkFields()) {
			MariaDBGeneroDAO generoDAO = new MariaDBGeneroDAO(MariaDBConexion.getConexion());
			try {
				generoDAO.guardar(new Genero(-1L, this.txtNombre.getText()));
				showValid("Creado con éxito");

			} catch (DAOException e) {
				showError("Error al crear");
				e.printStackTrace();
			}
		} else {
			showError("Rellene todos los campos");
		}
	}

	private boolean checkFields() {
		return (!this.txtNombre.getText().trim().isEmpty());
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
