package com.row49382;

import com.row49382.exception.ApplicationExecutionException;
import com.row49382.exception.EmailServiceException;
import com.row49382.exception.PairingGenerateException;
import com.row49382.service.Emailable;
import com.row49382.service.PairingGeneratable;
import com.row49382.util.impl.ApplicationPropertiesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private Invoker invoker;

    @BeforeEach
    public void setup() {
        this.invoker = new Invoker(
                this.applicationPropertiesManager,
                this.pairingGenerator,
                this.emailingService);
    }

    @Test
    void testInvokerRunsSuccessfully() throws PairingGenerateException, EmailServiceException, ApplicationExecutionException {
        doNothing().when(this.pairingGenerator).generate();
        doNothing().when(this.emailingService).send();
        when(this.applicationPropertiesManager.isSendEmail()).thenReturn(true);

        this.invoker.invoke();

        verify(this.pairingGenerator).generate();
        verify(this.emailingService).send();
        verify(this.applicationPropertiesManager).isSendEmail();
    }

    @Test
    void testWhenApplicationPropertiesSendEmailFalseEmailServiceNotInvoked() throws PairingGenerateException, ApplicationExecutionException, EmailServiceException {
        doNothing().when(this.pairingGenerator).generate();
        when(this.applicationPropertiesManager.isSendEmail()).thenReturn(false);

        this.invoker.invoke();

        verify(this.pairingGenerator).generate();
        verify(this.applicationPropertiesManager).isSendEmail();

        verify(this.emailingService, never()).send();
    }

    @Test
    void verifyWhenEmailServiceThrowsExceptionThatInvokerThrowsApplicationExecutionException() throws PairingGenerateException, EmailServiceException {
        doNothing().when(this.pairingGenerator).generate();
        when(this.applicationPropertiesManager.isSendEmail()).thenReturn(true);
        doThrow(EmailServiceException.class).when(this.emailingService).send();

        assertThrows(ApplicationExecutionException.class, () -> this.invoker.invoke());

        verify(this.pairingGenerator).generate();
        verify(this.applicationPropertiesManager).isSendEmail();
        verify(this.emailingService).send();
    }

    @Test
    void verifyWhenPairingGeneratorThrowsExceptionThatInvokerThrowsApplicationExecutionException() throws PairingGenerateException, EmailServiceException {
        doThrow(PairingGenerateException.class).when(this.pairingGenerator).generate();

        assertThrows(ApplicationExecutionException.class, () -> this.invoker.invoke());

        verify(this.pairingGenerator).generate();
        verify(this.applicationPropertiesManager, never()).isSendEmail();
        verify(this.emailingService, never()).send();
    }
}
