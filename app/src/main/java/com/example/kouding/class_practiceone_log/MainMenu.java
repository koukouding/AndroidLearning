package com.example.kouding.class_practiceone_log;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.*;
//import com.journeyapps.barcodescanner.CaptureActivity;
public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    private static final int TAKE_PHOTO_REQUEST_CODE =1 ;
    private ImageButton mTwodCodePic;
    private ImageButton mNetBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initView();
        setListener();
    }

    void initView() {

        mTwodCodePic = (ImageButton) findViewById(R.id.twodCodeBtn);
        mNetBtn=(ImageButton) findViewById(R.id.netBtn);
    }

    void setListener()
    {

        mTwodCodePic.setOnClickListener(this);
        mNetBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.twodCodeBtn:
                two2codeScan();
                break;
            case R.id.netBtn:
                startNetWatch();
                break;

        }
    }

    private void two2codeScan()
    {
        IntentIntegrator integrator =  new IntentIntegrator(this); // `this` is the current Activity
        integrator.setPrompt("请扫描"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(false); //扫描成功的「哔哔」声，默认开启
        integrator.initiateScan();
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      //  Intent takePictureIntent = new Intent(MainMenu.CaptureActivity.class);
       // startActivityForResult(takePictureIntent,TAKE_PHOTO_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void startNetWatch()
    {
        Intent intent = new Intent( this,NetWatch.class);
        intent.setAction(getPackageName()+".NetWatch");
        intent.setPackage(getPackageName());
        startService(intent);
        Toast.makeText(this, "开启网络监控", Toast.LENGTH_LONG).show();
    }
  //  @Override
  //  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);
    //}
}
