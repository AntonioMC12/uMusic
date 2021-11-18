package es.antoniomc.uMusic;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBListaReproduccionDAO;
import es.antoniomc.uMusic.model.ListaReproduccion;
import es.antoniomc.uMusic.model.usuarioHolder;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class crearPlaylistModalController {

	@FXML
	private Button btnCrear;

	@FXML
	private TextArea txtDescripcion;

	@FXML
	private TextField txtNombre;

	@FXML
	void crearPlaylist(ActionEvent event) {
		if (checkFields()) {
			MariaDBListaReproduccionDAO listaDAO = new MariaDBListaReproduccionDAO(MariaDBConexion.getConexion());
			try {
				listaDAO.guardar(new ListaReproduccion(-1L, this.txtNombre.getText(), this.txtDescripcion.getText(),
						usuarioHolder.getInstance().getUser()));
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
		return (!this.txtNombre.getText().trim().isEmpty() && !this.txtDescripcion.getText().trim().isEmpty());
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
