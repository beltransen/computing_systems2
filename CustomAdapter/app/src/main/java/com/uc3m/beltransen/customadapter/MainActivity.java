package com.uc3m.beltransen.customadapter;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView myListView;
    FloatingActionButton fab;
    private ArrayList<ListItem> myList;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myListView = (ListView) findViewById(R.id.my_listview);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        myList = new ArrayList<>();
        myList.add(new ListItem("Clase 1", "Intro a Android"));
        myList.add(new ListItem("Clase 2", "Activities"));
        myList.add(new ListItem("Clase 3", "Layouts"));

        final MyListAdapter listAdapter = new MyListAdapter(this, myList);
        myListView.setAdapter(listAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter%2==0){
                    myList.add(new ListItem("Clase "+ ++counter, "Titulo "+ counter));
                }else{
                    counter++;
                    myList.remove(0);
                }

                listAdapter.notifyDataSetChanged();
            }
        });
    }

    public class MyListAdapter extends BaseAdapter{

        private Context context;
        private ArrayList<ListItem> items;


        MyListAdapter(Context context, ArrayList<ListItem> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // inflate the layout for each list row
            if (convertView == null) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.list_item, parent, false);
            }

            // get current item to be displayed
            ListItem currentItem = (ListItem) getItem(position);

            // get the TextView for item name and item description
            TextView textViewItemName = (TextView)
                    convertView.findViewById(R.id.item_name);
            TextView textViewItemDescription = (TextView)
                    convertView.findViewById(R.id.item_description);

            //sets the text for item name and item description from the current item object
            textViewItemName.setText(currentItem.getName());
            textViewItemDescription.setText(currentItem.getDescription());

            // returns the view for the current row
            return convertView;
        }
    }
}
