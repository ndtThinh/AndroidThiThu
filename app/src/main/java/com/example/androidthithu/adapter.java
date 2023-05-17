package com.example.androidthithu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class adapter extends BaseAdapter implements Filterable {
    private ArrayList<Taxi> data;
    private ArrayList<Taxi> dataBackUp;
    private Activity context;
    private LayoutInflater inflater;

    public adapter() {
    }

    public adapter(ArrayList<Taxi> data, Activity context) {
        this.data = data;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.get(i).getMaId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = inflater.inflate(R.layout.layout_item_hoadon, null, true);
        }
        TextView soXe = v.findViewById(R.id.item_soxe);
        TextView quangDuong = v.findViewById(R.id.item_quangduong);
        TextView tongTien = v.findViewById(R.id.item_tongtien);

        soXe.setText(data.get(i).getSoXe());
        quangDuong.setText("Quãng đường: " + String.valueOf(data.get(i).getQuangDuong()) + " Km");
        tongTien.setText(String.valueOf(data.get(i).getDonGia() * data.get(i).getQuangDuong() * (100 - data.get(i).getKhuyenMai()) / 100));
        return v;
    }

    @Override
    public Filter getFilter() {
        Filter f = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults fr = new FilterResults();
                if (dataBackUp == null) {
                    dataBackUp = new ArrayList<>(data);
                }
                if (charSequence == null || charSequence.length() == 0) {
                    fr.count = dataBackUp.size();
                    fr.values = dataBackUp;
                } else {
                    ArrayList<Taxi> newData = new ArrayList<>();
                    double filterValues=Double.parseDouble(charSequence.toString());
                    for (Taxi x : dataBackUp) {
                        //tìm kiếm theo tên , biển số
//                        if (x.getSoXe().toLowerCase(Locale.ROOT).contains(charSequence.toString().toLowerCase())) {
//                            newData.add(x);
//                        }
                        //Tìm kiếm theo số tiền
                        double price=x.getDonGia() * x.getQuangDuong() * (100 - x.getKhuyenMai())/100;
                        if (price > filterValues){
                            newData.add(x);
                        }
                    }
                    fr.count = newData.size();
                    fr.values = newData;
                }
                return fr;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                data = new ArrayList<Taxi>();
                ArrayList<Taxi> tmp = (ArrayList<Taxi>) filterResults.values;
                for (Taxi x : tmp) {
                    data.add(x);
                }
                notifyDataSetChanged();
            }
        };
        return f;
    }
}
