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
    private String encryptionKey;
    private String encryptionScheme;
    public SecretKey secretKey;

    public CipherBase64() throws Exception {
        encryptionKey = "StreamFitCriptogra123456";
        encryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = encryptionKey.getBytes(UNICODE_FORMAT);
        keySpec = new DESedeKeySpec(arrayBytes);
        secretKeyFactory = SecretKeyFactory.getInstance(encryptionScheme);
        cipher = Cipher.getInstance(encryptionScheme);
        secretKey = secretKeyFactory.generateSecret(keySpec);
    }

    public String encrypt(String unencryptedString) {
        String encyptedString;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] text = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(text);
            encyptedString = new String(Base64.getEncoder().encode(encryptedText));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return encyptedString;
    }

    public String decrypt(String encryptedString) {
        String decryptedString;

        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encyptedText = Base64.getDecoder().decode(encryptedString);
            byte[] text = cipher.doFinal(encyptedText);
            decryptedString = new String(text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return decryptedString;
    }
}
