package es.antoniomc.uMusic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class crearModalController {

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
    public void createArtista(ActionEvent event) {
    	App.GoTo(event, "crearArtistaModal");
    }
    
    @FXML
    public void createDisco(ActionEvent event) {
    	App.GoTo(event, "crearDiscoModal");
    }
    
    @FXML
    public void createGenero(ActionEvent event) {
    	App.GoTo(event, "crearGeneroModal");
    }
    
    @FXML
    public void createCancion(ActionEvent event) {
    	App.GoTo(event, "crearCancionModal");
    }
    
    @FXML
    public void createPlaylist(ActionEvent event) {
    	App.GoTo(event, "crearPlaylistModal");
    }

}
