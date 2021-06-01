package com.chinews.xdapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Login extends AppCompatActivity {

    private String email;
    private String category;
    private String status;
    private String username;
    private String text;
    private String TEXT = "aaa";
    private StringBuilder json;

    @Nullable
    private static byte[] DecryptAES(byte[] iv, byte[] key, byte[] text) {
        try {
            AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec mSecretKeySpec = new SecretKeySpec(key, "AES");
            Cipher mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mCipher.init(Cipher.DECRYPT_MODE,
                    mSecretKeySpec,
                    mAlgorithmParameterSpec);

            return mCipher.doFinal(text);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("yyyyy", String.valueOf(ex));
            return null;
        }
    }

    public static boolean isEMUI() {
        String manufacturer = Build.MANUFACTURER;
        return "HUAWEI".equalsIgnoreCase(manufacturer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText editText = findViewById(R.id.editText);
        ImageView imageView = findViewById(R.id.imageView16);
        if (isEMUI() && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        imageView.setOnClickListener(v -> {
            text = String.valueOf(editText.getText());
            if (!text.equals("") && !text.equals("null")) {
                String IvAES = "u9ncRYMbGuSEp427";
                String KeyAES = "vNagtWc4h4gTfTAxv92WGSKq9fMU3tqQ";
                try {
                    byte[] TextByte;
                    TextByte = DecryptAES(IvAES.getBytes(StandardCharsets.UTF_8), KeyAES.getBytes(StandardCharsets.UTF_8), Base64.decode(text.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT));
                    TEXT = new String(TextByte, StandardCharsets.UTF_8);
                    json = new StringBuilder();
                    json.append(TEXT);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("yyyyy", String.valueOf(e));
                }
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(json));
                    username = jsonObject.getString("username");
                    email = jsonObject.getString("email");
                    category = jsonObject.getString("category");
                    status = jsonObject.getString("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast toast1 = Toast.makeText(this, "錯誤密碼，請重試", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.CENTER, 0, 0);
                    toast1.show();
                }
                if (username != null && email != null && category != null && status != null) {
                    Intent intent = new Intent();
                    intent.setClass(this, GoodLogin.class);
                    intent.putExtra("ajson", String.valueOf(TEXT));
                    startActivity(intent);
                } else {
                    // TODO: 18/2/2021
                    Toast toast1 = Toast.makeText(this, "錯誤密碼，請重試", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.CENTER, 0, 0);
                    toast1.show();
                }
            } else {
                Toast toast = Toast.makeText(this, "空密碼，請重試", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }
}