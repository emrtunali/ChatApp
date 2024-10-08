package com.example.chatapp.Model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatroomModel {
    String chatroomId;
    List<String> userIds;
    Timestamp lastMessageTimestamp;
    String lastMessageSenderId;
    String lastMessage;

    // Chat odalarını temsil eden model sınıfı.
    public ChatroomModel() {
    }

    public ChatroomModel(String chatroomId, List<String> userIds, Timestamp lastMessageTimestamp, String lastMessageSenderId) {
        // Parametresiz yapıcı.
        this.chatroomId = chatroomId;// Chat odasının ID'sini temsil eden özelliği.
        this.userIds = userIds;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageSenderId = lastMessageSenderId;
    }
    // Chat odasının ID'sini döndürür.
    public String getChatroomId() {
        return chatroomId;
    }
    // Chat odasının ID'sini ayarlar.

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }
    // Chat odasında bulunan kullanıcıların ID'lerini döndürür.

    public List<String> getUserIds() {
        return userIds;
    }

    // Chat odasında bulunan kullanıcıların ID'lerini ayarlar.
    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    // Chat odasındaki son mesajın zaman damgasını döndürür.
    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    // Chat odasındaki son mesajın zaman damgasını ayarlar.
    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    // Son mesajı gönderen kullanıcının ID'sini döndürür.
    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    // Son mesajı gönderen kullanıcının ID'sini ayarlar.
    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    // Chat odasındaki son mesajı döndürür.
    public String getLastMessage() {
        return lastMessage;
    }

    // Chat odasındaki son mesajı ayarlar.
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

}