package es.antoniomc.uMusic;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBGeneroDAO;
import es.antoniomc.uMusic.model.Genero;
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

public class editarGeneroModalController {

	@FXML
	private Button btnGuardar;

	@FXML
	private ComboBox<Genero> cbGenero;

	@FXML
	private TextField txtNombre;

	protected static ObservableList<Genero> generos = FXCollections.observableArrayList();

	public void initialize() {
		MariaDBGeneroDAO generoDAO = new MariaDBGeneroDAO(MariaDBConexion.getConexion());
		try {
			generos.setAll(generoDAO.mostrarTodos());
			this.cbGenero.setItems(generos);
		} catch (DAOException e) {
			showError("Error al obtener los datos");
			e.printStackTrace();
		}
	}

	@FXML
	void editarGenero(ActionEvent event) {
		if (checkFields()) {
			MariaDBGeneroDAO generoDAO = new MariaDBGeneroDAO(MariaDBConexion.getConexion());
			try {
				generoDAO.editar(new Genero(this.cbGenero.getValue().getId(), this.txtNombre.getText()));
				showValid("Editado con éxito");

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
	
	public void setCampos() {
		this.txtNombre.setText(this.cbGenero.getValue().getNombre());
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
