package com.paulathamyres.petshop.appointment;

import com.paulathamyres.petshop.pricing.PetSize;
import com.paulathamyres.petshop.pricing.ServiceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record CreateAppointmentRequest(

        @Schema(example = "1")
        @NotNull(message = "O cliente é obrigatório.")
        @Positive(message = "O id do cliente deve ser positivo.")
        Long customerId,

        @Schema(example = "Thor")
        @NotBlank(message = "O nome do pet é obrigatório.")
        @Size(max = 50, message = "O nome do pet deve ter no máximo 50 caracteres.")
        String petName,

        @NotNull(message = "O porte do pet é obrigatório.")
        PetSize petSize,

        @NotNull(message = "O tipo de serviço é obrigatório.")
        ServiceType serviceType,

        @Schema(example = "2030-01-15T10:00:00")
        @NotNull(message = "A data do agendamento é obrigatória.")
        @Future(message = "A data do agendamento deve estar no futuro.")
        LocalDateTime scheduledAt,

        @Schema(example = "true")
        @NotNull(message = "Informe se deseja o leva e traz.")
        Boolean pickupAndDelivery) {
}
