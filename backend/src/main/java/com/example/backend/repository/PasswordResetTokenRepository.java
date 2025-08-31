package com.example.backend.repository;

import com.example.backend.entity.PasswordResetToken;
import com.example.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // Buscar un token por su valor
    Optional<PasswordResetToken> findByToken(String token);

    // Eliminar tokens de un usuario específico
    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken t WHERE t.usuario = ?1")
    void deleteByUsuario(Usuario usuario);

    // Opcional: eliminar un token directamente por su valor
    @Modifying
    @Transactional
    void deleteByToken(String token);
}
