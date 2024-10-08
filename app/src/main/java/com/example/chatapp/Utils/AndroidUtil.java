package com.example.chatapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatapp.Model.UserModel;

public class AndroidUtil {
    // Belirtilen mesajı ekranda gösteren Toast mesajı fonksiyonu.

    public static  void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    // Bir Intent nesnesine UserModel nesnesini ekler. Bu, farklı aktiviteler arası kullanıcı bilgilerini taşımak için kullanılır.

    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("username",model.getUsername());// Kullanıcı bilgilerini Intent'e ekler.
        intent.putExtra("phone",model.getPhone());
        intent.putExtra("userId",model.getUserId());
        intent.putExtra("fcmToken",model.getFcmToken());

    }
  // Bir Intent'ten UserModel nesnesini alır. Bu, aktiviteler arası geçişlerde kullanıcı bilgilerini geri yüklemek için kullanılır.

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();// Kullanıcı bilgilerini içeren UserModel nesnesi
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUserId(intent.getStringExtra("userId"));
        userModel.setFcmToken(intent.getStringExtra("fcmToken"));
        return userModel;
    }

    // Kullanıcının profil resmini ImageView'a yükler. Glide kütüphanesi kullanılarak resim çevrimiçi olarak indirilip yuvarlak kesim ile gösterilir.

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
