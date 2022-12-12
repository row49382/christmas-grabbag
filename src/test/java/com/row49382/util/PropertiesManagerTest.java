package com.row49382.util;

import com.row49382.exception.PropertiesLoadException;
import com.row49382.util.impl.ApplicationPropertiesManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PropertiesManagerTest {

    @Test
    void testWhenPropertyLoadThrowsIOExceptionPropertyLoadExceptionIsThrownInstead() throws IOException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        Properties propertiesMock = mock(Properties.class);
        doThrow(IOException.class).when(propertiesMock).load(any(InputStream.class));

        PropertiesManager propertiesManager = new ApplicationPropertiesManager();
        Field propertiesField = propertiesManager
                .getClass()
                .getSuperclass()
                .getDeclaredField("properties");
        propertiesField.setAccessible(true);
        propertiesField.set(propertiesManager, propertiesMock);

        Method loadPropertiesMethod = propertiesManager
                .getClass()
                .getSuperclass()
                .getDeclaredMethod("loadProperties", String.class);
        loadPropertiesMethod.setAccessible(true);

        try {
            loadPropertiesMethod.invoke(
                    propertiesManager,
                    "application.properties");
        } catch (InvocationTargetException ite) {
            // reflection API throws an InvocationTargetException whenever an exception
            // is encountered when invoking a method/constructor. To verify that the PropertiesLoadException occurred
            // check the getCause() exception in the caught exception.
            assertEquals(PropertiesLoadException.class, ite.getCause().getClass());
        }
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "non-existent-properties.properties"})
    void verifyNullPointerExceptionIsThrownWhenFileValueIsNullOrNonExistent(String fileName) {
        assertThrows(NullPointerException.class, () -> new PropertiesManager(fileName) {});
    }
}
