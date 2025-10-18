// src/main/java/com/puc/realconsult/dto/UserResponseDTO.java
package com.puc.realconsult.dto;

import com.puc.realconsult.utils.StatusUsuario;

public record UserDTO (
        Long id,
        String nome,
        String cargo,
        String email,
        StatusUsuario status,
        String avatarColor
) {}
