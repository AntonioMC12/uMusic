package es.antoniomc.uMusic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class editarModalController {

    @FXML
    private Button btnArtista;

    @FXML
    private Button btnCancion;

    @FXML
    private Button btnDisco;

    @FXML
    private Button btnGenero;

    @FXML
    private Button btnPlaylist;
    
    @FXML
    public void editarArtista(ActionEvent event) {
    	App.GoTo(event, "editarArtistaModal");
    }
    
    @FXML
    public void editarCancion(ActionEvent event) {
    	App.GoTo(event, "editarCancionModal");
    }
    
    @FXML
    public void editarDisco(ActionEvent event) {
    	App.GoTo(event, "editarDiscoModal");
    }
    
    @FXML
    public void editarGenero(ActionEvent event) {
    	App.GoTo(event, "editarGeneroModal");
    }
    
    @FXML
    public void editarPlaylist(ActionEvent event) {
    	App.GoTo(event, "editarPlaylistModal");
    }

}
