package Projekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.ByteBuffer;
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
    public static byte[] binaryStringToByteArray(String binaryString) {
        binaryString = binaryString.replaceAll("\\s+", "");
        int length = binaryString.length();
        if (length % 8 != 0) {
            throw new IllegalArgumentException("Binary string length is not a multiple of 8");
        }
        byte[] byteArray = new byte[length / 8];
        for (int i = 0; i < length; i += 8) {
            String byteString = binaryString.substring(i, i + 8);
            byte b = (byte) Integer.parseInt(byteString, 2);
            byteArray[i / 8] = b;
        }
        return byteArray;
    }
    public static String byteArrayToBinaryString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
            sb.append(" ");
        }
        return sb.toString();
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    public static byte[] hexToBytes(String hex) {
        String cleanHex = hex.replaceAll("\\s+", "");
        int len = cleanHex.length();
        byte[] result = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            String hexByte = cleanHex.substring(i, i + 2); // Get each pair of hex characters
            byte b = (byte) Integer.parseInt(hexByte, 16); // Convert the hex byte to binary
            result[i / 2] = b; // Store the binary byte in the result array
        }
        return result;
    }



    public void EncryptText() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, Exception {
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
            //Gets byte array from plain text
            byte[] plaintextBytes;
            if(inpuTxt_ComboBox.getValue() == "Hexadecimal") {
                plaintextBytes = hexToBytes(plainTxt.getText());
            } else if (inpuTxt_ComboBox.getValue() == "Binary") {
//                plaintextBytes = binaryStringToByteArray(plainTxt.getText());
//                short a = Short.parseShort(plainTxt.getText(), 2);
//                ByteBuffer bytes = ByteBuffer.allocate(2).putShort(a);
                byte[] plainTextS =  binaryStringToByteArray(plainTxt.getText());
                plaintextBytes = plainTextS;
            } else {
                plaintextBytes = plainTxt.getText().getBytes(StandardCharsets.UTF_8);
            }
            // Encrypt the plaintext
            byte[] ciphertextBytes = cipher.doFinal(plaintextBytes);
            System.out.println("encoding: " + ciphertextBytes.length);
            // Encode the ciphertext using Base64 for display
            if (output_ComboBox.getValue().equals("Hexadecimal")) {
                cipherTxt.setText(bytesToHex(ciphertextBytes));
            } else if(output_ComboBox.getValue().equals("Text")){
                String ciphertext = Base64.getEncoder().encodeToString(ciphertextBytes);
                cipherTxt.setText(ciphertext);
            } else { // binary
                String txtBinary = byteArrayToBinaryString(ciphertextBytes);
                cipherTxt.setText(txtBinary);
            }
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
            byte[] ciphertextBytes;
            if(inpuTxt_ComboBox.getValue() == "Hexadecimal") {
                ciphertextBytes = hexToBytes(plainTxt.getText());
            } else if (inpuTxt_ComboBox.getValue() == "Binary") {
//                byte[] cipherTextS =  plainTxt.getText().replace(" ", "").getBytes(StandardCharsets.UTF_8);
                ciphertextBytes = binaryStringToByteArray(plainTxt.getText());
            } else {
                ciphertextBytes = Base64.getDecoder().decode(plainTxt.getText());
            }
            // Decrypt the ciphertext
            System.out.println("decoding: " + ciphertextBytes.length);
            byte[] plaintextBytes = cipher.doFinal(ciphertextBytes);
            // Convert the plaintext bytes to string
            if (output_ComboBox.getValue().equals("Hexadecimal")) {
                cipherTxt.setText(bytesToHex(plaintextBytes));
            } else if(output_ComboBox.getValue().equals("Text")){
                String plaintext = new String(plaintextBytes, StandardCharsets.UTF_8);
                cipherTxt.setText(plaintext);
            } else { // binary
                String plaintext = byteArrayToBinaryString(plaintextBytes);
                cipherTxt.setText(plaintext);
            }
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
        inpuTxt_ComboBox.setItems(list);
        inpuTxt_ComboBox.setValue("Text");
        output_ComboBox.setItems(list);
        output_ComboBox.setValue("Text");
        //Sets default key to "password"
        keyTxt.setText("password");
        //Sets default plaintext to "The brown fox jumps over the lazy dog."
        plainTxt.setText("The brown fox jumps over the lazy dog.");
    }

    public void randomizeKey(ActionEvent actionEvent) {
        String val = ""; // 65-90 letters 48-57 numbers,lowercase 97-112, these are for ascii? I played myself.
        for(int i = 0; i < 8; i++) {
            int randomB = (int)(Math.random() * 3) + 1;
            int randomNumber;
            if (randomB == 3) {
                randomNumber = (int)(Math.random() * 90) + 65;
            } else if(randomB == 2) {
                randomNumber = (int)(Math.random() * 57) + 48;
            } else {
                randomNumber = (int) (Math.random() * 112) + 97;
            }
            char v = (char)randomNumber;
            val += v;
        }
        keyTxt.setText(val);
    }
}



