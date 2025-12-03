package de.spree.keycloak.data_encrypt;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AES256StringConverterTest {

    private final AES256StringConverter underTest = new AES256StringConverter();

    @Test
    public void testEncodeToBase64() {
        String encodedString = underTest.convertToDatabaseColumn("geheim");

        assertEquals("vfGqH1dBABt9iSUNJu8oeRjxvn5SQg==", encodedString);
    }

    @Test
    public void testDecodeToBase64() {
        String decodecString = underTest.convertToEntityAttribute("vfGqH1dBABt9iSUNJu8oeRjxvn5SQg==");

        assertEquals("geheim", decodecString);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   ", "1@2.de", "äöüß", "normaler Text" })
    public void testEncodeAndDecodeTogether(String input) {
        String encodedString = underTest.convertToDatabaseColumn(input);
        String decodedString = underTest.convertToEntityAttribute(encodedString);

        assertEquals(input, decodedString);
    }


    @Test
    @Disabled("Not a test, just a helper to create some valid AES Key and some IV")
    public void generateSomeValidAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey key = keyGenerator.generateKey();
        String base64AESKeyString =  Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("base64AESKeyString: " + base64AESKeyString);

        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        System.out.println("iv: " + Base64.getEncoder().encodeToString(iv));
    }


}