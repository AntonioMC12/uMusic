package es.antoniomc.uMusic;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBListaReproduccionDAO;
import es.antoniomc.uMusic.model.ListaReproduccion;
import es.antoniomc.uMusic.model.usuarioHolder;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class editarPlaylistModalController {

	@FXML
	private Button btnGuardar;

	@FXML
	private ComboBox<ListaReproduccion> cbLista;

	@FXML
	private TextArea txtDescripcion;

	@FXML
	private TextField txtNombre;

	protected static ObservableList<ListaReproduccion> listas = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		MariaDBListaReproduccionDAO listaDAO = new MariaDBListaReproduccionDAO(MariaDBConexion.getConexion());
		try {
			listas.setAll(listaDAO.mostrarTodos());
			this.cbLista.setItems(listas);
		} catch (DAOException e) {
			showError("Error al obtener las listas");
			e.printStackTrace();
		}

	}

	@FXML
	void editarPlaylist(ActionEvent event) {
		if (checkFields()) {
			MariaDBListaReproduccionDAO listaDAO = new MariaDBListaReproduccionDAO(MariaDBConexion.getConexion());
			try {
				listaDAO.editar(new ListaReproduccion(this.cbLista.getValue().getId(), this.txtNombre.getText(), this.txtDescripcion.getText(),
						usuarioHolder.getInstance().getUser()));
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

	@FXML
	void setCampos(ActionEvent event) {
		this.txtNombre.setText(this.cbLista.getValue().getNombre());
		this.txtDescripcion.setText(this.cbLista.getValue().getDescripcion());
	}

}
