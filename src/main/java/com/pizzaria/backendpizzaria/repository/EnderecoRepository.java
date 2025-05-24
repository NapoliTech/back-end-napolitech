package com.pizzaria.backendpizzaria.repository;

import com.pizzaria.backendpizzaria.domain.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco,Integer> {

    @Query("SELECT e FROM Endereco e JOIN Usuario u ON e.usuarioId = u.idUsuario WHERE u.email = :email")
    Optional<Endereco> findByEmail(@Param("email") String email);
}
