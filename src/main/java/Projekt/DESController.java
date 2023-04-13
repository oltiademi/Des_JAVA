package Projekt;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import javax.crypto.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DESController {
    public TextField plainTxt;
    public TextField cipherTxt;
    public TextField decryptTxt;
    public TextField encryptTxt;
    public TextField keyTxt;

    public void encrypt(ActionEvent actionEvent) {
        String plain = plainTxt.getText();
        SecretKey key = DES.secretKey;
        /*
        pjesa e encryption
         */

        //encryptTxt.setText(encryptedText);
    }

    public void decrypt(ActionEvent actionEvent) {
        String plain = cipherTxt.getText();
        SecretKey key = DES.secretKey;
        /*
        pjesa e decryption
         */

        //decryptTxt.setText(encryptedText);
    }

    public void generateKey(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        SecretKey key = DES.generateKey();
        DES.secretKey = key;
        keyTxt.setText(String.valueOf(key));
    }
}

class DES {
    public static SecretKey secretKey;

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator Mygenerator = KeyGenerator.getInstance("DES");

        return Mygenerator.generateKey();
    }

}