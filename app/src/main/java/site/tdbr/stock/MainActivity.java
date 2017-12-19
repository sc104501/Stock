package site.tdbr.stock;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    public StockData stockData;
    private ItemListAdapter itemListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String stockPath = Environment.getExternalStorageDirectory().getPath() + "/0/Stock/";
        stockData = new StockData(stockPath);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
        try{
            init();
        }catch (Exception e){
            Log.e("init","Init Failed!");
        }

    }
    public void init()throws IOException,ParseException{
        stockData.init();
        itemListAdapter=new ItemListAdapter(stockData.items);
        ListView itemListView=findViewById(R.id.itemListView);
        itemListView.setAdapter(itemListAdapter);
    }
    private class ItemListAdapter extends BaseAdapter{
        private List<Item> data;
        public ItemListAdapter(List<Item> data){
            super();
            this.data=data;
        }

        @Override
        public int getCount() {
            return stockData.items.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.activity_main_itemlistview, viewGroup, false);
            }
            ((TextView) view.findViewById(R.id.Name)).setText(((Item)getItem(i)).name);
            ((TextView) view.findViewById(R.id.Price)).setText(((Item)getItem(i)).getPriceString());
            ((TextView) view.findViewById(R.id.Number)).setText(((Item)getItem(i)).stock);
            ((TextView) view.findViewById(R.id.Value)).setText(((Item)getItem(i)).getValueString());

            return view;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }


    }
}
