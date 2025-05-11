package com.pizzaria.backendpizzaria.service;

import com.pizzaria.backendpizzaria.domain.Endereco;
import com.pizzaria.backendpizzaria.domain.Enum.CategoriaProduto;
import com.pizzaria.backendpizzaria.domain.Produto;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.repository.EnderecoRepository;
import com.pizzaria.backendpizzaria.repository.ProdutoRepository;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DadosMockService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void carregarDados() {
        Usuario usuario = new Usuario();
        usuario.setNome("Marcos Paulo");
        usuario.setEmail("marcos@gmail.com");
        usuario.setSenha(passwordEncoder.encode("P@ssw0rd"));
        usuario.setCpf("123.123.123-11");
        usuario.setTelefone("(11) 99999-9999");
        usuario.setDataNasc("1989-01-10");
        usuario.setTipoUsuario("CLIENTE");
        usuario.setPedidos(0L);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Endereco endereco = new Endereco();
        endereco.setRua("Rua Haddock Lobo");
        endereco.setBairro("Consolação");
        endereco.setNumero(595);
        endereco.setComplemento("Digital Building");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01414-001");
        endereco.setUsuarioId(usuarioSalvo.getIdUsuario());

        enderecoRepository.save(endereco);

        Produto produto1 = new Produto();
        produto1.setNome("Pizza de Calabresa");
        produto1.setPreco(39.90);
        produto1.setQuantidadeEstoque(10);
        produto1.setIngredientes("Calabresa, Cebola, Orégano, Queijo Mussarela");
        produto1.setCategoriaProduto(CategoriaProduto.PIZZA);

        Produto produto2 = new Produto();
        produto2.setNome("Pizza de Palmito");
        produto2.setPreco(59.90);
        produto2.setQuantidadeEstoque(10);
        produto2.setIngredientes("Palmito, Cogumelo, Azeite");
        produto2.setCategoriaProduto(CategoriaProduto.PIZZA);

        Produto produto3 = new Produto();
        produto3.setNome("Coca-Cola Lata 350Ml");
        produto3.setPreco(5.90);
        produto3.setQuantidadeEstoque(25);
        produto3.setIngredientes("");
        produto3.setCategoriaProduto(CategoriaProduto.REFRIGERANTE);

        produtoRepository.save(produto1);
        produtoRepository.save(produto2);
        produtoRepository.save(produto3);
    }
}
