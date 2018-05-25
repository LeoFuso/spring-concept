package model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.UUID;

@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, String> {

    @Override
    public String convertToDatabaseColumn(UUID attribute) {
        return Optional.ofNullable(attribute)
                .map(UUID::toString)
                .orElse(null);
    }

    @Override
    public UUID convertToEntityAttribute(String dbData) {
        return Optional.ofNullable(dbData)
                .map(UUID::fromString)
                .orElse(null);
    }
}
