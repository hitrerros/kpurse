package ru.hitrerros.purse.util;


import ru.hitrerros.purse.utils.ReflectionHelper;
import ru.hitrerros.purse.message.Document;
import ru.hitrerros.purse.model.PurseEntity;

import java.lang.reflect.Field;
import java.util.Optional;

public class DocumentProcessHelper {

    private DocumentProcessHelper(){}


    public static Document copyEntityToDocument(PurseEntity entity) {
        Document doc = new Document();

        for (Field field : Document.class.getDeclaredFields()) {
            Optional<Object> value = Optional.of(ReflectionHelper.getFieldValue(entity,field.getName()));
            value.ifPresent(v->ReflectionHelper.setFieldValue(doc,field.getName(),v));
        }

        return doc;
    }

}
