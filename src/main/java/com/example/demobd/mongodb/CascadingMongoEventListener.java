package com.example.demobd.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.data.mapping.MappingException;

import java.lang.reflect.Field;
import java.util.Collection;

@SuppressWarnings("rawtypes")
@Component
public class CascadingMongoEventListener extends AbstractMongoEventListener {
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(BeforeConvertEvent event) {
        ReflectionUtils.doWithFields(event.getSource().getClass(), field -> {
            ReflectionUtils.makeAccessible(field);

            if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeSave.class)) {
                final Object fieldValue = field.get(event.getSource());

                if (fieldValue != null) {
                    if (fieldValue instanceof Collection) {
                        saveCollectionElements((Collection) fieldValue);
                    } else {
                        verifyObjectHasId(fieldValue);
                        mongoOperations.save(fieldValue);
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void saveCollectionElements(Collection fieldValue) {
        fieldValue.forEach(object -> {
            verifyObjectHasId(object);
            mongoOperations.save(object);
        });
    }

    private void verifyObjectHasId(Object object) {
        DbRefFieldCallback callback = new DbRefFieldCallback();

        ReflectionUtils.doWithFields(object.getClass(), callback);
        if (!callback.isIdFound()) {
            throw new MappingException("Cannot perform cascade save on child object without id set");
        }
    }

    private static class DbRefFieldCallback implements ReflectionUtils.FieldCallback {
        private boolean idFound;

        public void doWith(Field field) throws IllegalArgumentException {
            ReflectionUtils.makeAccessible(field);

            if (field.isAnnotationPresent(Id.class)) {
                idFound = true;
            }
        }

        public boolean isIdFound() {
            return idFound;
        }
    }
}
