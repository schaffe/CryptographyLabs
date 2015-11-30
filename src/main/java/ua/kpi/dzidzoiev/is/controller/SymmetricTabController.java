package ua.kpi.dzidzoiev.is.controller;

import com.sun.javafx.collections.ImmutableObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import ua.kpi.dzidzoiev.is.service.symmetric.CipherAlgsEnum;
import ua.kpi.dzidzoiev.is.service.symmetric.CipherModeEnum;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by dzidzoiev on 11/30/15.
 */
public class SymmetricTabController implements Initializable {

    public static final Logger LOG = Logger.getLogger(SymmetricTabController.class.getName());

    public ToggleButton sym_tog_key_auto;
    public TextField sym_edit_key;
    public ToggleButton sym_tog_file_auto;
    public TextField sym_edit_source_file;
    public TextField sym_edit_dest_file;
    public ChoiceBox<CipherModeEnum> sym_drop_enc_mode;
    public ChoiceBox<CipherAlgsEnum> sym_drop_alg;
    public Button sym_btn_encrypt;
    public Button sym_btn_select_source;
    public Button sym_btn_select_dest;
    public Button sym_btn_decrypt;
    public GridPane sym_root;

    private boolean autoSelectKey;
    private String key;
    private boolean autoCreateFile;
    private File sourceFile;
    private File destFile;
    private String cipherMode;
    private String cipherAlg;

    public void initialize(URL location, ResourceBundle resources) {
        sym_tog_key_auto.setSelected(true);
        sym_tog_key_auto.setOnAction(e -> {
            boolean selected = sym_tog_key_auto.isSelected();
            if (selected) {
                sym_edit_key.setDisable(true);
                setKey(getNewRandomKey());
            } else {
                sym_edit_key.setDisable(false);
                setKey("");
            }
            sym_edit_key.setText(getKey());
        });

        sym_edit_key.setDisable(true);
        sym_edit_key.setOnKeyReleased(e -> {
                    setKey(sym_edit_key.getText());
                    System.out.println(getKey());
                }
        );

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
        sym_edit_source_file.setPromptText("Файл генерується автоматично");

        sym_btn_select_source.setDisable(true);
        sym_btn_select_source.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Вихідний файл");
            File chosenFile = fileChooser.showOpenDialog(sym_root.getScene().getWindow());
            if (chosenFile != null) {
                LOG.info("Source file chosen " + chosenFile.getAbsolutePath());
                setSourceFile(chosenFile);
            }
        });

        sym_btn_select_dest.setOnAction(e -> {
            DirectoryChooser fileChooser = new DirectoryChooser();
            fileChooser.setTitle("Папка для збереження");
            File chosenFile = fileChooser.showDialog(sym_root.getScene().getWindow());
            if (chosenFile != null) {
                LOG.info("Dest file chosen " + chosenFile.getAbsolutePath());
                setDestFile(chosenFile);
            }
        });

        sym_drop_enc_mode.setItems(new ImmutableObservableList<>(CipherModeEnum.values()));
        sym_drop_enc_mode.setValue(CipherModeEnum.CBC);
        sym_drop_alg.setItems(new ImmutableObservableList<>(CipherAlgsEnum.values()));
        sym_drop_alg.setValue(CipherAlgsEnum.DES);
    }

    private String getNewRandomKey() {
        return "stub";
    }

    public String getKey() {
        return key;
    }

    public SymmetricTabController setKey(String key) {
        this.key = key;
        return this;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public SymmetricTabController setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
        sym_edit_source_file.setText(sourceFile.getAbsolutePath());
        return this;
    }

    public File getDestFile() {
        return destFile;
    }

    public SymmetricTabController setDestFile(File destFile) {
        this.destFile = destFile;
        sym_edit_dest_file.setText(destFile.getAbsolutePath());
        return this;
    }
}
