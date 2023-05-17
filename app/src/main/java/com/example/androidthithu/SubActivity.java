package com.example.androidthithu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubActivity extends AppCompatActivity {

    private EditText edsoXe, edquangDuong, edDonGia, edKhuyenMai;
    private Button btnBack;
    private Button btnSua;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        edsoXe = findViewById(R.id.edit_soxe);
        edquangDuong = findViewById(R.id.edit_quangDuong);
        edDonGia = findViewById(R.id.edit_dongia);
        edKhuyenMai = findViewById(R.id.edit_khuyeMai);
        btnBack = findViewById(R.id.btn_quaylai);
        btnSua = findViewById(R.id.btn_sua);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int state = bundle.getInt("state");
            if (state == 0) {
                    btnSua.setText("ADD");
            }
            if (state == 1) {
                id = bundle.getInt("Id");
                String soXe = bundle.getString("SoXe");
                double quangDuong = bundle.getDouble("QuangDuong");
                int donGia = bundle.getInt("DonGia");
                int khuyenMai = bundle.getInt("KhuyenMai");
                edsoXe.setText(soXe);
                edquangDuong.setText(String.valueOf(quangDuong));
                edDonGia.setText(String.valueOf(donGia));
                edKhuyenMai.setText(String.valueOf(khuyenMai));
            }
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edDonGia.getText().toString().equals("") || edKhuyenMai.getText().toString().equals("") || edquangDuong.getText().toString().equals("")
                        || edsoXe.getText().toString().equals("")) {
                    Toast.makeText(SubActivity.this, "Not empty", Toast.LENGTH_SHORT).show();
                } else {
                    String soXe = String.valueOf(edsoXe.getText());
                    double quangDuong = Double.parseDouble(edquangDuong.getText().toString());
                    int donGia = Integer.parseInt(edDonGia.getText().toString());
                    int khuyenMai = Integer.parseInt(edKhuyenMai.getText().toString());
                    Intent intent1 = new Intent();
                    Bundle b = new Bundle();
                    b.putInt("Id", id);
                    b.putString("SoXe", soXe);
                    b.putDouble("QuangDuong", quangDuong);
                    b.putInt("DonGia", donGia);
                    b.putInt("KhuyenMai", khuyenMai);
                    intent1.putExtras(b);
                    setResult(150, intent1);
                    finish();
                }
            }
        });
    }
}