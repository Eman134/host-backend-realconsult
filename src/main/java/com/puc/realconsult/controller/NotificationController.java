package com.puc.realconsult.controller;

import com.puc.realconsult.model.Notification;
import com.puc.realconsult.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Endpoint para enviar uma notificação para um usuário
    @PostMapping("/enviar/{usuarioId}")
    public ResponseEntity<String> enviarNotificacao(@PathVariable Long usuarioId, @RequestBody String mensagem) {
        notificationService.enviarNotificacao(usuarioId, mensagem);
        return ResponseEntity.ok("Notificação enviada!");
    }

    // Endpoint para enviar uma notificação para um cargo
    @PostMapping("/enviar/cargo/{cargo}")
    public ResponseEntity<String> enviarNotificacaoParaCargo(@PathVariable String cargo, @RequestBody String mensagem) {
        notificationService.enviarNotificacaoParaCargo(cargo, mensagem);
        return ResponseEntity.ok("Notificação enviada para o cargo!");
    }

    // Endpoint para marcar uma notificação como lida
    @PostMapping("/marcar-como-lido/{notificationId}")
    public ResponseEntity<String> marcarComoLido(@PathVariable Long notificationId) {
        notificationService.marcarComoLido(notificationId);
        return ResponseEntity.ok("Notificação marcada como lida!");
    }

    // Endpoint para marcar uma notificação como não lida
    @PostMapping("/marcar-como-nao-lido/{notificationId}")
    public ResponseEntity<String> marcarComoNaoLido(@PathVariable Long notificationId) {
        notificationService.marcarComoNaoLido(notificationId);
        return ResponseEntity.ok("Notificação marcada como não lida!");
    }

    // Endpoint para obter notificações não lidas de um usuário
    @GetMapping("/nao-lidas/{usuarioId}")
    public ResponseEntity<List<Notification>> obterNotificacoesNaoLidas(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "10") int quantidade
    ) {
        List<Notification> notifications = notificationService.obterNotificacoesNaoLidas(usuarioId, quantidade);
        return ResponseEntity.ok(notifications);
    }

    // Endpoint para obter notificações lidas de um usuário
    @GetMapping("/lidas/{usuarioId}")
    public ResponseEntity<List<Notification>> obterNotificacoesLidas(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "10") int quantidade
    ) {
        List<Notification> notifications = notificationService.obterNotificacoesLidas(usuarioId, quantidade);
        return ResponseEntity.ok(notifications);
    }


}
