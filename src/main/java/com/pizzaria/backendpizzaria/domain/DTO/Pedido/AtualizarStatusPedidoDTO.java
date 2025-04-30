package com.pizzaria.backendpizzaria.domain.DTO.Pedido;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa os dados necessários para atualizar o status de um pedido.")
public class AtualizarStatusPedidoDTO {
    @Schema(description = "Novo status do pedido.", example = "ENTREGUE", required = true)
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
