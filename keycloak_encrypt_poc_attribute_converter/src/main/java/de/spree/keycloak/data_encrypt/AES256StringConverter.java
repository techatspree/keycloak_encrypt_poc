package de.spree.keycloak.data_encrypt;

import jakarta.persistence.AttributeConverter;
import org.jboss.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES Encryption based on https://www.baeldung.com/java-aes-encryption-decryption
 */
public class AES256StringConverter implements AttributeConverter<String, String> {

    private static final Logger logger = Logger.getLogger(AES256StringConverter.class);

    // Some example KEY: must be externalized in a secure way
    private static final byte[] KEY = Base64.getDecoder().decode("pAO73OKjmLHD+O7izzWo/2l74R3W+nOkwT8rOHomgNs=");;
    // Some example Init Vector: must be externalized in a secure way
    private static final byte[] INITIALIZATION_VECTOR = Base64.getDecoder().decode("PCOLK+at87vmeH1W");;

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    @Override
    public String convertToDatabaseColumn(String attribute) {
        logger.debugf("Converting attribute '%s'", attribute);
        if (attribute == null) {
            return null;
        }
        try {
            return encrypt(attribute, KEY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        logger.debugf("Converting dbData '%s'", dbData);
        if (dbData == null) {
            return null;
        }

        try {
            return decrypt(dbData, KEY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String encrypt(String data, byte[] key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, getGCMParameterSpec(INITIALIZATION_VECTOR));
        byte[] encryptedData = cipher.doFinal(data.getBytes());

        return Base64.getEncoder().encodeToString(encryptedData);
    }

    private String decrypt(String encryptedData, byte[] key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, getGCMParameterSpec(INITIALIZATION_VECTOR));
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] originalData = cipher.doFinal(decodedData);

        return new String(originalData);
    }

    private GCMParameterSpec getGCMParameterSpec(byte[] iv) {
        return new GCMParameterSpec(128, iv);
    }
}
