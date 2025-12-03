package de.spree.keycloak.data_encrypt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleBase64StringConverterTest {

    @Test
    public void testEncodeToBase64() {
        SimpleBase64StringConverter converter = new SimpleBase64StringConverter();
        String encodedString = converter.convertToDatabaseColumn("geheim");

        assertEquals("Z2VoZWlt", encodedString);
    }

    @Test
    public void testDecodeToBase64() {
        SimpleBase64StringConverter converter = new SimpleBase64StringConverter();
        String decodecString = converter.convertToEntityAttribute("Z2VoZWlt");

        assertEquals("geheim", decodecString);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   ", "1@2.de", "äöüß" })
    public void testEncodeAndDecodeTogether(String input) {
        SimpleBase64StringConverter converter = new SimpleBase64StringConverter();
        String encodedString = converter.convertToDatabaseColumn(input);
        String decodedString = converter.convertToEntityAttribute(encodedString);

        assertEquals(input, decodedString);
    }
}