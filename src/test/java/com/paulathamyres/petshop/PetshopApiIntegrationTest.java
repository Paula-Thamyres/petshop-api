package com.paulathamyres.petshop;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.paulathamyres.petshop.address.Address;
import com.paulathamyres.petshop.address.AddressLookupService;
import com.paulathamyres.petshop.shared.exception.ExternalServiceException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class PetshopApiIntegrationTest {

    private static final String ARARAS_CEP = "13600-061";
    private static final Address ARARAS_ADDRESS =
            new Address(ARARAS_CEP, "Rua Júlio Mesquita", "Centro", "Araras", "SP");

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private AddressLookupService addressLookupService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
    }

    @Test
    @DisplayName("Cadastra o cliente, agenda um banho com leva e traz e consulta o agendamento")
    void registersCustomerAndSchedulesBathWithPickup() throws Exception {
        long customerId = registerCustomerInAraras();

        String appointmentJson = mockMvc.perform(post("/appointments")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": %d,
                                  "petName": "Thor",
                                  "petSize": "MEDIUM",
                                  "serviceType": "BATH",
                                  "scheduledAt": "2099-12-01T10:00:00",
                                  "pickupAndDelivery": true
                                }
                                """.formatted(customerId)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.servicePrice").value(70.0))
                .andExpect(jsonPath("$.pickupFee").value(15.0))
                .andExpect(jsonPath("$.totalPrice").value(85.0))
                .andReturn().getResponse().getContentAsString();

        Number appointmentId = JsonPath.read(appointmentJson, "$.id");

        mockMvc.perform(get("/appointments/{id}", appointmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Maria Souza"))
                .andExpect(jsonPath("$.petName").value("Thor"));
    }

    @Test
    @DisplayName("Devolve 400 com a lista de campos inválidos e não consulta o CEP")
    void rejectsInvalidCustomer() throws Exception {
        mockMvc.perform(post("/customers")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name": "", "phone": "123", "cep": "abc"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Dados inválidos"))
                .andExpect(jsonPath("$.errors.length()").value(3))
                .andExpect(jsonPath("$.errors[0].field").value("cep"))
                .andExpect(jsonPath("$.errors[1].field").value("name"))
                .andExpect(jsonPath("$.errors[2].field").value("phone"));

        verifyNoInteractions(addressLookupService);
    }

    @Test
    @DisplayName("Devolve 400 quando o porte do pet não existe")
    void rejectsUnknownPetSize() throws Exception {
        mockMvc.perform(post("/appointments")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": 1,
                                  "petName": "Thor",
                                  "petSize": "HUGE",
                                  "serviceType": "BATH",
                                  "scheduledAt": "2099-12-01T10:00:00",
                                  "pickupAndDelivery": false
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Corpo da requisição inválido"));
    }

    @Test
    @DisplayName("Devolve 404 ao agendar para um cliente que não existe")
    void rejectsUnknownCustomer() throws Exception {
        mockMvc.perform(post("/appointments")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": 9999,
                                  "petName": "Thor",
                                  "petSize": "SMALL",
                                  "serviceType": "WALK",
                                  "scheduledAt": "2099-12-01T10:00:00",
                                  "pickupAndDelivery": false
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Cliente não encontrado: 9999"));
    }

    @Test
    @DisplayName("Devolve 502 quando o ViaCEP está fora do ar")
    void returnsBadGatewayWhenCepServiceIsDown() throws Exception {
        when(addressLookupService.findByCep(anyString()))
                .thenThrow(new ExternalServiceException("Não foi possível consultar o CEP agora.", new RuntimeException()));

        mockMvc.perform(post("/customers")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name": "Maria Souza", "phone": "19999990000", "cep": "13600-061"}
                                """))
                .andExpect(status().isBadGateway());
    }

    @Test
    @DisplayName("Documenta no Swagger os códigos que a API realmente devolve")
    void documentsActualResponseCodes() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/customers'].post.responses['201']").exists())
                .andExpect(jsonPath("$.paths['/customers'].post.responses['200']").doesNotExist())
                .andExpect(jsonPath("$.paths['/customers'].post.responses['422']").exists())
                .andExpect(jsonPath("$.paths['/appointments'].post.responses['201']").exists())
                .andExpect(jsonPath("$.paths['/appointments'].post.responses['200']").doesNotExist())
                .andExpect(jsonPath("$.paths['/appointments/{id}'].get.responses['404']").exists());
    }

    private long registerCustomerInAraras() throws Exception {
        when(addressLookupService.findByCep(ARARAS_CEP)).thenReturn(ARARAS_ADDRESS);

        String customerJson = mockMvc.perform(post("/customers")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name": "Maria Souza", "phone": "19999990000", "cep": "13600-061"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.address.city").value("Araras"))
                .andReturn().getResponse().getContentAsString();

        Number customerId = JsonPath.read(customerJson, "$.id");
        return customerId.longValue();
    }
}
