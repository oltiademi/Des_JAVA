package Projekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.ResourceBundle;
public class DESController implements Initializable {
    @FXML
    private TextArea cipherTxt;
    @FXML
    private Button decryptBtn;
    @FXML
    private ComboBox comboBox_key;
    @FXML
    private ComboBox inpuTxt_ComboBox;
    @FXML
    private ComboBox output_ComboBox;
    @FXML
    private Button encryptBtn;
    @FXML
    private TextField keyTxt;
    @FXML
    private TextArea plainTxt;
    String keyText;

    public void EncryptText() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {

        keyText = keyTxt.getText();
        if (keyText.length() == 8) {
            // Convert the password to bytes
            byte[] passwordBytes = keyText.getBytes(StandardCharsets.UTF_8);
            // Create a KeySpec for DES
            KeySpec keySpec = new DESKeySpec(passwordBytes);
            // Creates a SecretKeyFactory for DES
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            // Generates a DES key from the KeySpec
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            //Defines the encryption algorithm
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            //Gets byte array from string in the UTF-8 format
            byte[] plaintextBytes = plainTxt.getText().getBytes(StandardCharsets.UTF_8);
            // Encrypt the plaintext
            byte[] ciphertextBytes = cipher.doFinal(plaintextBytes);
            // Encode the ciphertext using Base64 for display
            String ciphertext = Base64.getEncoder().encodeToString(ciphertextBytes);
            cipherTxt.setText(ciphertext);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("This is an alert!");
            alert.setContentText("Qelesi duhet te jete 8 karaktere!!");
            alert.showAndWait();
        }
    }
    public void Decrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        keyText = keyTxt.getText();
        if(keyText.length() == 8) {
            // Convert the password to bytes
            byte[] passwordBytes = keyText.getBytes(StandardCharsets.UTF_8);
            // Create a KeySpec for DES
            KeySpec keySpec = new DESKeySpec(passwordBytes);
            // Create a SecretKeyFactory for DES
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            // Generate a DES key from the KeySpec
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            //Defines the decryption algorithm
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            //Gets byte array from the ciphertext
            byte[] ciphertextBytes = Base64.getDecoder().decode(plainTxt.getText());
            // Decrypt the ciphertext
            byte[] plaintextBytes = cipher.doFinal(ciphertextBytes);
            // Convert the plaintext bytes to string
            String plaintext = new String(plaintextBytes, StandardCharsets.UTF_8);
            cipherTxt.setText(plaintext);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("This is an alert!");
            alert.setContentText("Qelesi duhet te jete 8 karaktere!!");
            alert.showAndWait();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> list = FXCollections.observableArrayList("Text", "Binary", "Hexadecimal");
        comboBox_key.setItems(list);
        comboBox_key.setValue("Hexadecimal");
        inpuTxt_ComboBox.setItems(list);
        inpuTxt_ComboBox.setValue("Text");
        output_ComboBox.setItems(list);
        output_ComboBox.setValue("Text");
        //Sets default key to "password"
        keyTxt.setText("password");
        //Sets default plaintext to "The brown fox jumps over the lazy dog."
        plainTxt.setText("The brown fox jumps over the lazy dog.");
    }

}



