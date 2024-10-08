package com.example.chatapp.Model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String phone;
    private String username;
    private Timestamp createdTimestamp;
    private String userId;
    private String fcmToken;

    // Boş constructor, Firebase gibi ORM araçları tarafından nesne oluşturulurken kullanılır.
    public UserModel() {
    }

    // Kullanıcı bilgileri ile dolu constructor.
    public UserModel(String phone, String username, Timestamp createdTimestamp, String userId) {

        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
    }

    // Kullanıcının telefon numarasını döndürür.
    public String getPhone() {
        return phone;
    }

    // Kullanıcının telefon numarasını ayarlar.
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Kullanıcının kullanıcı adını döndürür.
    public String getUsername() {
        return username;
    }

    // Kullanıcının kullanıcı adını ayarlar.
    public void setUsername(String username) {
        this.username = username;
    }

    // Kullanıcının hesabının oluşturulma zamanını döndürür.
    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    // Kullanıcının hesabının oluşturulma zamanını ayarlar.
    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    // Kullanıcının benzersiz ID'sini döndürür.
    public String getUserId() {
        return userId;
    }

    // Kullanıcının benzersiz ID'sini ayarlar.
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Kullanıcının FCM token'ını döndürür.
    public String getFcmToken() {
        return fcmToken;
    }

    // Kullanıcının FCM token'ını ayarlar.
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}