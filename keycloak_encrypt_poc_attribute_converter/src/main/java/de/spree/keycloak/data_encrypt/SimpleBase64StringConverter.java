package de.spree.keycloak.data_encrypt;

import jakarta.persistence.AttributeConverter;
import org.jboss.logging.Logger;

import java.util.Base64;

public class SimpleBase64StringConverter implements AttributeConverter<String, String> {

    private static final Logger logger = Logger.getLogger(SimpleBase64StringConverter.class);

    @Override
    public String convertToDatabaseColumn(String attribute) {
        logger.debugf("Converting attribute '%s'", attribute);
        if (attribute == null) {
            return null;
        }
        return Base64.getMimeEncoder().encodeToString(attribute.getBytes());
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        logger.debugf("Converting dbData '%s'", dbData);
        if (dbData == null) {
            return null;
        }
        byte[] decode = Base64.getMimeDecoder().decode(dbData);
        return new String(decode);
    }
}
