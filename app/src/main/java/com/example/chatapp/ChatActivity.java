package com.example.chatapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.ChatRecyclerAdapter;
import com.example.chatapp.Model.ChatMessageModel;
import com.example.chatapp.Model.ChatroomModel;
import com.example.chatapp.Model.UserModel;
import com.example.chatapp.Utils.AndroidUtil;
import com.example.chatapp.Utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {


    UserModel otherUser;
    String chatroomId;
    ChatroomModel chatroomModel;
    ChatRecyclerAdapter adapter;

    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton backBtn;
    TextView otherUsername;
    RecyclerView recyclerView;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // Diğer kullanıcının bilgilerini intent'ten alır.

        //get UserModel
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(),otherUser.getUserId());
        // Görünüm bileşenleri tanımlanır.

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        backBtn = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);
        imageView = findViewById(R.id.profile_pic_image_view);

        // Diğer kullanıcının profil resmini yükler.

        FirebaseUtil.getOtherProfilePicStorageRef(otherUser.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if(t.isSuccessful()){
                        Uri uri  = t.getResult();
                        AndroidUtil.setProfilePic(this,uri,imageView);
                    }
                });
        // Geri dönme butonunun olay dinleyicisi.

        backBtn.setOnClickListener((v)->{
            onBackPressed();
        });
        otherUsername.setText(otherUser.getUsername());
        // Mesaj gönderme butonu için olay dinleyicisi.

        sendMessageBtn.setOnClickListener((v -> {
            // Mesajın içeriğini alır.
            String message = messageInput.getText().toString().trim();// Mesajın boş olup olmadığını kontrol eder.
            if(message.isEmpty())
                return;
            sendMessageToUser(message);// Kullanıcıya mesaj gönderir.
        }));

        getOrCreateChatroomModel();
        setupChatRecyclerView();// Mesaj listesi için RecyclerView'i ayarlar.
    }
    // Mesaj listesi için RecyclerView'i ayarlar.
    void setupChatRecyclerView(){
        // Firestore'tan mesajları alınır.
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)// Mesajları chat odası id'sine göre sıralar.
                .orderBy("timestamp", Query.Direction.DESCENDING);// Mesajları tarihe göre sıralar.

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()// Firestore'tan mesajları alınır
                .setQuery(query,ChatMessageModel.class).build();// Mesajları RecyclerView'e bağlar.

        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    // Kullanıcıya mesaj gönderir ve chat odası modelini günceller.

    void sendMessageToUser(String message){

        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message,FirebaseUtil.currentUserId(),Timestamp.now());// Mesajın içeriğini alır.
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)// Mesajları chat odası id'sine göre sıralar.
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {// Mesajları tarihe göre sıralar.
                        if(task.isSuccessful()){
                            messageInput.setText("");// Mesaj gönderildiğinde mesaj inputunu temizler.
                            sendNotification(message);
                        }
                    }
                });
    }

    // Chat odası modelini alır veya ilk kez chat yapılıyorsa oluşturur.

    void getOrCreateChatroomModel(){
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                chatroomModel = task.getResult().toObject(ChatroomModel.class);
                if(chatroomModel==null){
                    // İlk mesaj için chat odası oluşturma.

                    chatroomModel = new ChatroomModel(// Kullanıcıları chat odası id'sine göre sıralar.
                            chatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(),otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);
                }
            }
        });
    }

    // Diğer kullanıcıya bildirim gönderir.
    void sendNotification(String message){// Kullanıcıya bildirim gönderir.

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                UserModel currentUser = task.getResult().toObject(UserModel.class);
                try{
                    JSONObject jsonObject  = new JSONObject();

                    JSONObject notificationObj = new JSONObject();
                    notificationObj.put("title",currentUser.getUsername());
                    notificationObj.put("body",message);

                    JSONObject dataObj = new JSONObject();
                    dataObj.put("userId",currentUser.getUserId());

                    jsonObject.put("notification",notificationObj);
                    jsonObject.put("data",dataObj);
                    jsonObject.put("to",otherUser.getFcmToken());

                    callApi(jsonObject);


                }catch (Exception e){

                }

            }
        });

    }

    // Bildirim için HTTP API çağrısı yapar.
    void callApi(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization","AAAAvbvNcjk:APA91bE4FFa66XzzwbsXRs1iqQeyjb6dZOPxZnPPpPpjl-rUAdA3_uSrUp81ZJEySZLjRHg03aWdGMfFL0jJIRlxb3w9yIZ8xE88C407FjeAw9jdyQ5WGvZrYVvJ9OeVcvkVugS36UyG")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
// Bildirim gönderildiğinde yapılacak işlemler.
            }
        });

    }
}