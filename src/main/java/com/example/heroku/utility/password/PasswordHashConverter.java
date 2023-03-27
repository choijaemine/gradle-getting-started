package com.example.heroku.utility.password;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.example.heroku.utility.encrypt.EncryptionUtil;

@Converter
public class PasswordHashConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return EncryptionUtil.generateHash(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
