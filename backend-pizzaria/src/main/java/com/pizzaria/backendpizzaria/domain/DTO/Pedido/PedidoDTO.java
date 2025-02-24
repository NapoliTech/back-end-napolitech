package com.pizzaria.backendpizzaria.domain.DTO.Pedido;
import com.pizzaria.backendpizzaria.domain.Enum.StatusPedido;
import com.pizzaria.backendpizzaria.domain.Pedido;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PedidoDTO {
    private Long pedidoId;
    private Long clienteId;
    private String nomeCliente;
    private Double valorTotal;
    private List<ItemPedidoDTO> itens;
    private String telefone;
    private StatusPedido statusPedido;
}
