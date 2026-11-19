package com.paulathamyres.petshop.address;

import com.paulathamyres.petshop.shared.exception.BusinessRuleException;
import com.paulathamyres.petshop.shared.exception.ExternalServiceException;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class AddressLookupService {

    private static final Logger log = LoggerFactory.getLogger(AddressLookupService.class);
    private static final Pattern NON_DIGIT = Pattern.compile("\\D");

    private final ViaCepClient viaCepClient;

    public AddressLookupService(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    public Address findByCep(String cep) {
        String digits = NON_DIGIT.matcher(cep).replaceAll("");
        ViaCepResponse response = fetch(digits);
        if (response == null || response.isNotFound()) {
            throw new BusinessRuleException("CEP não encontrado: " + cep);
        }
        return response.toAddress();
    }

    private ViaCepResponse fetch(String cep) {
        try {
            return viaCepClient.findByCep(cep);
        } catch (RestClientException ex) {
            log.warn("ViaCEP lookup failed for CEP {}", cep, ex);
            throw new ExternalServiceException(
                    "Não foi possível consultar o CEP agora. Tente novamente em instantes.", ex);
        }
    }
}
