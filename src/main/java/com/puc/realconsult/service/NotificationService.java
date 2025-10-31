package com.puc.realconsult.service;

import com.puc.realconsult.model.Notification;
import com.puc.realconsult.model.UserModel;
import com.puc.realconsult.repository.NotificationRepository;
import com.puc.realconsult.repository.UserRepository;
import com.puc.realconsult.utils.StatusNotificacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository usuarioRepository;

    public void enviarNotificacao(Long usuarioId, String mensagem) {
        Optional<UserModel> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            Notification notification = new Notification();
            notification.setUsuario(usuario.get());
            notification.setMensagem(mensagem);
            notification.setStatus(StatusNotificacao.NAO_LIDO);
            notification.setDataCriacao(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }

    public void enviarNotificacaoParaCargo(String cargo, String mensagem) {
        List<UserModel> usuarios = usuarioRepository.findByCargo(cargo);
        for (UserModel usuario : usuarios) {
            enviarNotificacao(usuario.getId(), mensagem);
        }
    }

    public void enviarNotificacaoParaCargos(String[] cargos, String mensagem) {
        List<UserModel> usuarios = usuarioRepository.findByCargoIn(Arrays.asList(cargos));

        for (UserModel usuario : usuarios) {
            enviarNotificacao(usuario.getId(), mensagem);
        }
    }


    public void marcarComoLido(Long notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (notification.isPresent()) {
            Notification notif = notification.get();
            notif.setStatus(StatusNotificacao.LIDO);
            notificationRepository.save(notif);
        }
    }

    public void marcarComoNaoLido(Long notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (notification.isPresent()) {
            Notification notif = notification.get();
            notif.setStatus(StatusNotificacao.NAO_LIDO);
            notificationRepository.save(notif);
        }
    }

    public List<Notification> obterNotificacoesNaoLidas(Long usuarioId, int quantidade) {
        Pageable pageable = PageRequest.of(0, quantidade, Sort.by(Sort.Order.desc("dataCriacao")));
        return notificationRepository.findByUsuarioIdAndStatus(usuarioId, StatusNotificacao.NAO_LIDO, pageable).getContent();
    }

    public List<Notification> obterNotificacoesLidas(Long usuarioId, int quantidade) {
        Pageable pageable = PageRequest.of(0, quantidade, Sort.by(Sort.Order.desc("dataCriacao")));
        return notificationRepository.findByUsuarioIdAndStatus(usuarioId, StatusNotificacao.LIDO, pageable).getContent();
    }

}

