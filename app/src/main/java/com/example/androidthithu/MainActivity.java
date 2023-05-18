package com.example.androidthithu;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Taxi> TaxiList;
    private adapter ListAdapter;
    private SqliteDB_10012002 db;
    private ListView listViewTaxi;
    int SelectItemId;
    private EditText editSearch;
    private Button btnAdd;
    //thêm broadcast
    private ConnectionReceiver connectionReceiver;
    private IntentFilter intentFilter;
    public void onBroadcast() {
        connectionReceiver = new ConnectionReceiver();
        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectionReceiver, intentFilter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectionReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectionReceiver,intentFilter );
    }
    //--------------end broadcast

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewTaxi = findViewById(R.id.listview_taxi);
        editSearch=findViewById(R.id.edit_search);
        btnAdd=findViewById(R.id.btn_add);
        TaxiList = new ArrayList<>();
        db = new SqliteDB_10012002(this, "Taxi_DB", null, 1);

//        db.clearData();
//       db.addTaxi(new Taxi("17B2-65890",10.5,150,5));
//       db.addTaxi(new Taxi("19B6-12346",5.5,100,3));
//        db.addTaxi(new Taxi("18B7-12315",7,200,4));
//        db.addTaxi(new Taxi("450B7-12315",7,200,4));
//        db.addTaxi(new Taxi("dethi01",7,200,4));

        TaxiList = db.getAllTaxi();
//        Toast.makeText(this, "Số lượng phần tử: "+TaxiList.size(), Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //sắp thêm giảm dần:   TaxiList.sort(Comparator.comparing(Taxi::getSoXe).reversed());
            TaxiList.sort(Comparator.comparing(Taxi::getSoXe)); //sắp xếp tăng dần
        }
        ListAdapter = new adapter(TaxiList, this);
        listViewTaxi.setAdapter(ListAdapter);

        //thêm cái này để có thể mở context menu
        registerForContextMenu(listViewTaxi);
        listViewTaxi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                SelectItemId = i;
//                double price=TaxiList.get(SelectItemId).getQuangDuong()*TaxiList.get(SelectItemId).getDonGia()*(100-TaxiList.get(SelectItemId).getKhuyenMai())/100;
//                int count=0;
//                for (Taxi x:TaxiList) {
//                    if(x.getQuangDuong()*x.getDonGia()*(100-x.getKhuyenMai())/100 >price){
//                        count++;
//                    }
//                }
//                String message="Nguyễn Đức Thịnh- số hóa đơn: "+count;
//                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    ListAdapter.getFilter().filter(charSequence.toString());
                    ListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(MainActivity.this,SubActivity.class);
                Bundle b1=new Bundle();
                b1.putInt("state",0);
                intent1.putExtras(b1);
                startActivityForResult(intent1,130);
            }
        });

        //gọi hàm broadcast
        onBroadcast();
        ListAdapter.notifyDataSetChanged();

    }
    //het ham oncreate


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.contextmenu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Taxi taxi = TaxiList.get(SelectItemId);
        switch (item.getItemId()) {
            case R.id.menu_sua:
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("state",1);
                bundle.putInt("Id", taxi.getMaId());
                bundle.putString("SoXe", taxi.getSoXe());
                bundle.putDouble("QuangDuong", taxi.getQuangDuong());
                bundle.putInt("DonGia", taxi.getDonGia());
                bundle.putInt("KhuyenMai", taxi.getKhuyenMai());
                intent.putExtras(bundle);
                Toast.makeText(this, "id sửa: "+taxi.getMaId(), Toast.LENGTH_SHORT).show();
                startActivityForResult(intent, 100);
                break;
            case R.id.menu_xoa:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm")
                        .setMessage("Do you want to delete?")
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.deleteTaxi(taxi);
                                TaxiList.remove(SelectItemId);
                                ListAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                builder.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        db = new SqliteDB_10012002(this, "Taxi_DB", null, 1);

        //sua
        if (requestCode == 100 && resultCode == 150) {
            Bundle b = data.getExtras();
            int newId = b.getInt("Id");
            String soXe = b.getString("SoXe");
            double quangDuong = b.getDouble("QuangDuong");
            int donGia = b.getInt("DonGia");
            int khuyenMai = b.getInt("KhuyenMai");
            Taxi taxi = new Taxi(newId, soXe, quangDuong, donGia, khuyenMai);
            db.upDateTaxi(taxi);
            TaxiList.remove(SelectItemId);
            TaxiList.add(SelectItemId, taxi);
            Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show();
        }
        //them
        if(requestCode==130&& resultCode==150){
            Bundle b = data.getExtras();
            String soXe = b.getString("SoXe");
            double quangDuong = b.getDouble("QuangDuong");
            int donGia = b.getInt("DonGia");
            int khuyenMai = b.getInt("KhuyenMai");
            Taxi taxi = new Taxi( soXe, quangDuong, donGia, khuyenMai);
            db.addTaxi(taxi);
            TaxiList.add(taxi);
            Toast.makeText(this, "id taxi moi: "+ taxi.getMaId(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "add successfully", Toast.LENGTH_SHORT).show();
        }

        ListAdapter.notifyDataSetChanged();
        TaxiList.sort(Comparator.comparing(Taxi::getSoXe));
    }
}