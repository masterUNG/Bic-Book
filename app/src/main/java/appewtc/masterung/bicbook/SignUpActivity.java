package appewtc.masterung.bicbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    //Explicit
    private EditText idCardEditText, passwordEditText;
    private String idCardString, passwordString;
    private String strURL = "http://swiftcodingthai.com/bic/php_add_user_master.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Bind Widget
        bindWidget();

    }   // Main Method

    private void bindWidget() {
        idCardEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);
    }

    public void clickSignUpSign(View view) {

        idCardString = idCardEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //Check Space
        if (idCardString.equals("") || passwordString.equals("")) {
            //Have Space
            myToast("กรุณากรอกให้ครบ คะ");

        } else {
            //No Space
            checkIDcard();
        }


    }   // clickSignUp

    private void checkIDcard() {

        if (idCardString.length() == 13) {
            //id card True
            confirmData(idCardString, passwordString);

        } else {
            //id card False
            myToast("รหัสบัตรไม่ถูกต้อง");

        }

    }   // chekcIDcard

    private void confirmData(String idCardString, String passwordString) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon_myaccount);
        builder.setTitle("โปรดตรวจสอบข้อมูล");
        builder.setMessage("รหัสบัตรประชาชน = " + idCardString + "\n" +
                "Password = " + passwordString);
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //updateDataToServer();

                //Try use OKhttp
                updateByOKhttp();

                dialogInterface.dismiss();
            }   // onClick
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }   // onClick
        });
        builder.show();


    }   // confirmData

    private void updateByOKhttp() {

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("isAdd", "true")
                .add("ID_Card", idCardString)
                .add("Password", passwordString)
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(strURL).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(SignUpActivity.this, "Cannot Update",
                        Toast.LENGTH_SHORT).show();
            }   //onFailure

            @Override
            public void onResponse(Response response) throws IOException {

                try {

                    Log.d("12April", "Response  ==> " + response.body().string());

                } catch (Exception e) {
                    Log.d("12April", "Error e ==> " + e.toString());
                }

            }   //onResponse
        });

    }   // update

    private void updateDataToServer() {

        //Connected Http
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("isAdd", "true"));
            nameValuePairs.add(new BasicNameValuePair("ID_Card", idCardString));
            nameValuePairs.add(new BasicNameValuePair("Password", passwordString));

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://swiftcodingthai.com/bic/php_add_user_master.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            httpClient.execute(httpPost);
            myToast("อัพเดทข้อมูล บน Server แว้วววว");
            finish();

        } catch (Exception e) {
            myToast("ไม่สามารถเชื่อมต่อ Server ได้");
        }




    }   // updateDataToServer

    private void myToast(String strToase) {
        Toast.makeText(SignUpActivity.this, strToase, Toast.LENGTH_SHORT).show();
    }

}   // Main Class
