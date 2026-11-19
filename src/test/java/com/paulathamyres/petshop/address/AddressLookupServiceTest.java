package com.paulathamyres.petshop.address;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.paulathamyres.petshop.shared.exception.BusinessRuleException;
import com.paulathamyres.petshop.shared.exception.ExternalServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.ResourceAccessException;

@ExtendWith(MockitoExtension.class)
class AddressLookupServiceTest {

    @Mock
    private ViaCepClient viaCepClient;

    private AddressLookupService service;

    @BeforeEach
    void setUp() {
        service = new AddressLookupService(viaCepClient);
    }

    @Test
    @DisplayName("Consulta o ViaCEP só com os dígitos e devolve o endereço")
    void returnsAddressUsingOnlyDigits() {
        when(viaCepClient.findByCep("13600061"))
                .thenReturn(new ViaCepResponse("13600-061", "Rua Júlio Mesquita", "Centro", "Araras", "SP"));

        Address address = service.findByCep("13600-061");

        assertThat(address.getCep()).isEqualTo("13600-061");
        assertThat(address.getStreet()).isEqualTo("Rua Júlio Mesquita");
        assertThat(address.getCity()).isEqualTo("Araras");
        assertThat(address.getState()).isEqualTo("SP");
    }

    @Test
    @DisplayName("Recusa CEP que não existe no ViaCEP")
    void rejectsUnknownCep() {
        when(viaCepClient.findByCep("99999999"))
                .thenReturn(new ViaCepResponse(null, null, null, null, null));

        assertThatThrownBy(() -> service.findByCep("99999-999"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("CEP não encontrado: 99999-999");
    }

    @Test
    @DisplayName("Traduz falha de comunicação com o ViaCEP em erro de serviço externo")
    void translatesCommunicationFailure() {
        when(viaCepClient.findByCep("13600061")).thenThrow(new ResourceAccessException("timeout"));

        assertThatThrownBy(() -> service.findByCep("13600061"))
                .isInstanceOf(ExternalServiceException.class)
                .hasCauseInstanceOf(ResourceAccessException.class);
    }
}
