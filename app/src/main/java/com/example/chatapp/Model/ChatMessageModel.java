package com.example.chatapp.Model;

import com.google.firebase.Timestamp;

public class ChatMessageModel {
    private String message;
    private String senderId;
    private Timestamp timestamp;

    public ChatMessageModel() {
    }
// Chat mesajlarını temsil eden model sınıfı.

    // Mesaj, gönderen ID ve zaman bilgisi ile dolu constructor.

    public ChatMessageModel(String message, String senderId, Timestamp timestamp) {

        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }
    // Boş constructor, Firebase gibi ORM araçları tarafından nesne oluşturulurken kullanılır.

    // Mesaj metnini döndürür.
    public String getMessage() {
        return message;
    }

    // Mesaj metnini ayarlar.

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }
    // Mesajı gönderen kullanıcının ID'sini döndürür.

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    // Mesajı gönderen kullanıcının ID'sini ayarlar.

    public Timestamp getTimestamp() {
        return timestamp;
    }
    // Mesajın gönderilme zamanını döndürür.


    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
