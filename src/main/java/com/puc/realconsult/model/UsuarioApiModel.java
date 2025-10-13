/* TODO essa classe não deve ser modificada e nem utilizada para outro fim a não ser junção de cliente
     e usuario que fazem requisicoes via api da VTReal
*/

package com.puc.realconsult.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UsuarioApiModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(unique = true)
    private String login;
    @JoinColumn(name = "id_cliente")
    @OneToOne
    private ClienteModel idCliente;
    private String email;

}
