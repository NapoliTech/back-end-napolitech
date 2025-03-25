package com.pizzaria.backendpizzaria.service;

import com.pizzaria.backendpizzaria.domain.DTO.Pedido.ProdutoDTO;
import com.pizzaria.backendpizzaria.domain.Produto;
import com.pizzaria.backendpizzaria.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public Produto cadastrarProduto(ProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produto.setQuantidadeEstoque(produtoDTO.getQuantidade());

        return produtoRepository.save(produto);
    }

    public Optional<Produto> listarProdutoPorId(Integer id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    @Transactional
    public boolean deletarProduto(Integer id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            produtoRepository.delete(produtoOptional.get());
            return true;
        }

        return false;
    }
}