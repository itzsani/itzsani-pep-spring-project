package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<?> createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!accountRepository.existsById(message.getPostedBy())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Message saved = messageRepository.save(message);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public ResponseEntity<Message> getMessageById(Integer id) {
        Optional<Message> message = messageRepository.findById(id);
        return message.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.OK));
    }

    public List<Message> getMessagesByUser(Integer userId) {
        return messageRepository.findByPostedBy(userId);
    }

    public ResponseEntity<Integer> updateMessage(Integer id, Message newData) {
        Optional<Message> existing = messageRepository.findById(id);
        if (existing.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (newData.getMessageText() == null || newData.getMessageText().isBlank() || newData.getMessageText().length() > 255) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Message message = existing.get();
        message.setMessageText(newData.getMessageText());
        messageRepository.save(message);
        return new ResponseEntity<>(1, HttpStatus.OK);
    }

    public ResponseEntity<Integer> deleteMessage(Integer id) {
        boolean exists = messageRepository.existsById(id);
        if (!exists) return new ResponseEntity<>(HttpStatus.OK);
        messageRepository.deleteById(id);
        return new ResponseEntity<>(1, HttpStatus.OK);
    }
    
}
