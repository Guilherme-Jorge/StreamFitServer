package services;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class CipherBase64 {
    private static final String UNICODE_FORMAT = "UTF-8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec keySpec;
    private SecretKeyFactory secretKeyFactory;
    private Cipher cipher;
    public byte[] arrayBytes;
    private String encryptionScheme;
    public SecretKey secretKey;

    public CipherBase64() throws Exception {
        String encryptionKey = "StreamFitCriptogra123456";
        this.encryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        this.arrayBytes = encryptionKey.getBytes(UNICODE_FORMAT);
        this.keySpec = new DESedeKeySpec(this.arrayBytes);
        this.secretKeyFactory = SecretKeyFactory.getInstance(this.encryptionScheme);
        this.cipher = Cipher.getInstance(this.encryptionScheme);
        this.secretKey = secretKeyFactory.generateSecret(this.keySpec);
    }

    public String encrypt(String unencryptedString) {
        String encyptedString;

        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
            byte[] text = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = this.cipher.doFinal(text);
            encyptedString = new String(Base64.getEncoder().encode(encryptedText));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return encyptedString;
    }

    public String decrypt(String encryptedString) {
        String decryptedString;

        try {
            this.cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encyptedText = Base64.getDecoder().decode(encryptedString);
            byte[] text = this.cipher.doFinal(encyptedText);
            decryptedString = new String(text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return decryptedString;
    }
}
