package es.antoniomc.uMusic;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBCancionDAO;
import es.antoniomc.uMusic.model.Cancion;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;

public class borrarCancionModalController {

	@FXML
	private Button btnBorrar;

	@FXML
	private ComboBox<Cancion> cbCancion;

	protected static ObservableList<Cancion> canciones = FXCollections.observableArrayList();

	public void initialize() {
		this.btnBorrar.setDisable(true);
		MariaDBCancionDAO cancionDAO = new MariaDBCancionDAO(MariaDBConexion.getConexion());

		try {
			canciones.setAll(cancionDAO.mostrarTodos());
			this.cbCancion.setItems(canciones);
		} catch (DAOException e) {
			showError("Error al obtener los datos");
			e.printStackTrace();
		}
	}

	@FXML
	void borrar(ActionEvent event) {
		MariaDBCancionDAO cancionDAO = new MariaDBCancionDAO(MariaDBConexion.getConexion());
		try {
			cancionDAO.borrar(this.cbCancion.getValue());
			canciones.remove(this.cbCancion.getValue());
			this.cbCancion.setItems(canciones);
			showValid("Borrado con éxito");
		} catch (DAOException e) {
			showError("Error al Borrar");
			e.printStackTrace();
		}
	}

	@FXML
	void setButton(ActionEvent event) {
		if(this.cbCancion.getValue()!=null) {
			this.btnBorrar.setDisable(false);
		}
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
