package ua.kpi.dzidzoiev.is.controller;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import ua.kpi.dzidzoiev.is.service.FileEncryptService;
import ua.kpi.dzidzoiev.is.service.FileHelper;
import ua.kpi.dzidzoiev.is.service.SymmetricService;
import ua.kpi.dzidzoiev.is.service.symmetric.CipherDescription;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ua.kpi.dzidzoiev.is.service.Holder.Log;

/**
 * Created by dzidzoiev on 11/30/15.
 */
public class SymmetricTabController implements Initializable {

    public TextField sym_edit_key;
    public ToggleButton sym_tog_file_auto;
    public TextField sym_edit_source_file;
    public TextField sym_edit_dest_file;
//    public ChoiceBox<CipherDescription> sym_drop_enc_mode;
    public ChoiceBox<CipherDescription> sym_drop_alg;
    public Button sym_btn_encrypt;
    public Button sym_btn_select_source;
    public Button sym_btn_select_dest;
    public Button sym_btn_decrypt;
    public GridPane sym_root;
    public Button sym_btn_refresh_key;

    private SymmetricService symmetricService;
    private FileEncryptService fileEncryptService;
    private List<CipherDescription> algs;

    public SymmetricTabController() {
        symmetricService = new SymmetricService();
        fileEncryptService = new FileEncryptService();
        algs = getAlgorithms();
    }

    public void initialize(URL location, ResourceBundle resources) {
//        sym_tog_key_auto.setSelected(true);
//
//        sym_tog_key_auto.setOnAction(e -> {
//            boolean selected = sym_tog_key_auto.isSelected();
//            if (selected) {
//                sym_edit_key.setEditable(false);
//                setKey(getNewRandomKey());
//            } else {
//                sym_edit_key.setEditable(true);
//                setKey("");
//            }
//            sym_edit_key.setText(getKey());
//        });

//        sym_edit_key.setEditable(false);
//        sym_edit_key.setOnKeyReleased(e -> {
//                    setKey(sym_edit_key.getText());
//                    System.out.println(getKey());
//                }
//        );

        sym_tog_file_auto.setSelected(true);
        sym_tog_file_auto.setOnAction(e -> {
            if (sym_tog_file_auto.isSelected()) {
                sym_edit_source_file.setDisable(true);
                sym_edit_source_file.setPromptText("Файл генерується автоматично");
                sym_btn_select_source.setDisable(true);
            } else {
                sym_edit_source_file.setDisable(false);
                sym_edit_source_file.setPromptText("Вкажіть вихідний файл");
                sym_btn_select_source.setDisable(false);
            }
        });

        sym_edit_source_file.setDisable(true);
        sym_edit_source_file.setEditable(false);
        sym_edit_source_file.setText(FileHelper.getTempFile());
//        sym_edit_source_file.setPromptText("Файл генерується автоматично");

        sym_btn_select_source.setDisable(true);
        sym_btn_select_source.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Вихідний файл");
            File chosenFile = fileChooser.showOpenDialog(sym_root.getScene().getWindow());
            if (chosenFile != null) {
                String absolutePath = chosenFile.getAbsolutePath();
                Log.info("Source file chosen " + absolutePath);
                sym_edit_source_file.setText(absolutePath);
                sym_edit_dest_file.setText(FileHelper.getDir(absolutePath));
            }
        });

//        sym_edit_dest_file.setEditable(false);
        sym_edit_dest_file.setText(FileHelper.getTempFolder());
        sym_btn_select_dest.setOnAction(e -> {
            DirectoryChooser fileChooser = new DirectoryChooser();
            fileChooser.setTitle("Папка для збереження");
            File chosenFile = fileChooser.showDialog(sym_root.getScene().getWindow());
            if (chosenFile != null) {
                String absolutePath = chosenFile.getAbsolutePath();
                Log.info("Dest file chosen " + absolutePath);
                sym_edit_dest_file.setText(absolutePath);
            }
        });

        sym_drop_alg.setItems(FXCollections.observableArrayList(algs));
        sym_drop_alg.getSelectionModel().selectFirst();

        sym_btn_encrypt.setOnAction(e -> {
            String newFile = fileEncryptService.encrypt(
                    sym_edit_source_file.getText(),
                    sym_edit_dest_file.getText(),
                    sym_drop_alg.getValue(),
                    sym_edit_key.getText());
            sym_edit_source_file.setText(newFile);
        });

        sym_btn_decrypt.setOnAction( e -> {
            fileEncryptService.decrypt(
                    sym_edit_source_file.getText(),
                    sym_edit_dest_file.getText(),
                    sym_drop_alg.getValue(),
                    sym_edit_key.getText());
        });

        sym_btn_refresh_key.setOnAction(e -> {
            regenerateKey();
        });
        regenerateKey();
    }

    private void regenerateKey() {
        sym_edit_key.setText(getNewRandomKey());
    }

    private String getNewRandomKey() {
        return symmetricService.generateKey(getSelectedCipherDescription());
    }

    private CipherDescription getSelectedCipherDescription() {
        return sym_drop_alg.getSelectionModel().selectedItemProperty().get();
    }

    public List<CipherDescription> getAlgorithms() {
        return symmetricService.getAvailableCiphers().stream()
                .sorted((c1, c2) -> c1.toString().compareTo(c2.toString()))
                .collect(Collectors.toList());
    }
}
