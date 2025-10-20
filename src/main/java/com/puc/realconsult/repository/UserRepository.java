package com.puc.realconsult.repository;

import java.util.List;

import com.puc.realconsult.utils.StatusUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.puc.realconsult.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    
    @Query("SELECT u FROM UserModel u WHERE " +
           "LOWER(u.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(u.cargo) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<UserModel> buscarPorTermo(@Param("termo") String termo);
    
    List<UserModel> findByStatus(StatusUsuario status);
    
    boolean existsByEmail(String email);
    
    boolean existsByEmailAndIdNot(String email, Long id);

    UserModel findByEmail(String email);

    //Buscas no Header
    List<UserModel> findByNomeContainingIgnoreCase(String nome);
}