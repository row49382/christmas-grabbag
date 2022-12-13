package com.row49382;

import com.row49382.exception.ApplicationExecutionException;
import com.row49382.exception.EmailServiceException;
import com.row49382.exception.PairingGenerateException;
import com.row49382.service.Emailable;
import com.row49382.service.FileWritable;
import com.row49382.service.PairingGeneratable;
import com.row49382.util.impl.ApplicationPropertiesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvokerTest {

    @Mock
    private ApplicationPropertiesManager applicationPropertiesManager;

    @Mock
    private PairingGeneratable pairingGenerator;

    @Mock
    private Emailable emailingService;

    @Mock
    private FileWritable resultsWriter;

    private Invoker invoker;

    @BeforeEach
    public void setup() {
        this.invoker = new Invoker(
                this.applicationPropertiesManager,
                this.pairingGenerator,
                this.emailingService,
                this.resultsWriter);
    }

    @Test
    void testInvokerRunsSuccessfully() throws PairingGenerateException, EmailServiceException, ApplicationExecutionException {
        doNothing().when(this.pairingGenerator).generate();
        doNothing().when(this.emailingService).send();
        when(this.applicationPropertiesManager.doSendEmail()).thenReturn(true);
        when(this.applicationPropertiesManager.doSaveResults()).thenReturn(true);

        this.invoker.invoke();

        verify(this.pairingGenerator).generate();
        verify(this.emailingService).send();
        verify(this.applicationPropertiesManager).doSendEmail();
        verify(this.applicationPropertiesManager).doSaveResults();
    }

    @Test
    void testWhenApplicationPropertiesSendEmailFalseEmailServiceNotInvoked() throws PairingGenerateException, ApplicationExecutionException, EmailServiceException {
        doNothing().when(this.pairingGenerator).generate();
        when(this.applicationPropertiesManager.doSendEmail()).thenReturn(false);

        this.invoker.invoke();

        verify(this.pairingGenerator).generate();
        verify(this.applicationPropertiesManager).doSendEmail();

        verify(this.emailingService, never()).send();
    }

    @Test
    void verifyWhenEmailServiceThrowsExceptionThatInvokerThrowsApplicationExecutionException() throws PairingGenerateException, EmailServiceException {
        doNothing().when(this.pairingGenerator).generate();
        when(this.applicationPropertiesManager.doSendEmail()).thenReturn(true);
        doThrow(EmailServiceException.class).when(this.emailingService).send();

        assertThrows(ApplicationExecutionException.class, () -> this.invoker.invoke());

        verify(this.pairingGenerator).generate();
        verify(this.applicationPropertiesManager).doSendEmail();
        verify(this.emailingService).send();
    }

    @Test
    void verifyWhenPairingGeneratorThrowsExceptionThatInvokerThrowsApplicationExecutionException() throws PairingGenerateException, EmailServiceException {
        doThrow(PairingGenerateException.class).when(this.pairingGenerator).generate();

        assertThrows(ApplicationExecutionException.class, () -> this.invoker.invoke());

        verify(this.pairingGenerator).generate();
        verify(this.applicationPropertiesManager, never()).doSendEmail();
        verify(this.emailingService, never()).send();
    }

    @Test
    void verifyWhenResultsWriterThrowsIOExceptionInvokerThrowsApplicationExecutionException() throws PairingGenerateException, EmailServiceException, IOException {
        doNothing().when(this.pairingGenerator).generate();
        when(this.applicationPropertiesManager.doSendEmail()).thenReturn(true);

        doNothing().when(this.emailingService).send();

        doThrow(IOException.class).when(this.resultsWriter).write();
        when(this.applicationPropertiesManager.doSaveResults()).thenReturn(true);

        assertThrows(ApplicationExecutionException.class, () -> this.invoker.invoke());

        verify(this.pairingGenerator).generate();
        verify(this.applicationPropertiesManager).doSendEmail();
        verify(this.emailingService).send();
        verify(this.applicationPropertiesManager).doSaveResults();
        verify(this.resultsWriter).write();
    }
}
