package es.antoniomc.uMusic;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBListaReproduccionDAO;
import es.antoniomc.uMusic.model.ListaReproduccion;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;

public class borrarPlaylistModalController {

    @FXML
    private Button btnBorrar;

    @FXML
    private ComboBox<ListaReproduccion> cbPlaylist;

	protected static ObservableList<ListaReproduccion> listas = FXCollections.observableArrayList();

	public void initialize() {
		this.btnBorrar.setDisable(true);
		MariaDBListaReproduccionDAO listaDAO = new MariaDBListaReproduccionDAO(MariaDBConexion.getConexion());
		try {
			listas.setAll(listaDAO.mostrarTodos());
			this.cbPlaylist.setItems(listas);
		} catch (DAOException e) {
			showError("Error al obtener las listas");
			e.printStackTrace();
		}
	}

	@FXML
	void borrar(ActionEvent event) {
		MariaDBListaReproduccionDAO listaDAO = new MariaDBListaReproduccionDAO(MariaDBConexion.getConexion());
		try {
			listaDAO.borrar(this.cbPlaylist.getValue());
			listas.remove(this.cbPlaylist.getValue());
			this.cbPlaylist.setItems(listas);
			showValid("Borrado con éxito");
		} catch (DAOException e) {
			showError("Error al Borrar");
			e.printStackTrace();
		}
	}

	@FXML
	void setButton(ActionEvent event) {
		if(this.cbPlaylist.getValue()!=null) {
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
