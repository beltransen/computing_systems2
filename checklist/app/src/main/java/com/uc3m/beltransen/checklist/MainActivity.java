package com.uc3m.beltransen.checklist;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity{

    ChecklistCollectionPageAdapter checklistCollectionPageAdapter;
    ViewPager viewPager;
    ArrayList<CheckList> checkLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_newlist:
                        Intent i = new Intent(MainActivity.this, NewListActivity.class);
                        startActivityForResult(i, 0); // TODO REQUEST CODE
                        break;
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        checkLists = new ArrayList<CheckList>();
        checklistCollectionPageAdapter = new ChecklistCollectionPageAdapter(getSupportFragmentManager(), checkLists);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(checklistCollectionPageAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    protected void onResume() {
        super.onResume();

        checkLists.clear();
        try {
            Scanner scanner = new Scanner(getApplicationContext().openFileInput("checkLists.txt"));
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                CheckList g = new CheckList(line);
                checkLists.add(g);
            }
        }catch (IOException e) {
            return;
        }
        checklistCollectionPageAdapter.setData(checkLists);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case RESULT_OK:
                String name = data.getStringExtra("name");
                Log.d("MainActivity","New list created: '"+name+"'");
                // Create and append new checklist
                checkLists.add(new CheckList(name));
                updateLists(); // Update files and UI
                viewPager.setCurrentItem(checkLists.size()); // Move pager to new checklist
                break;
            case RESULT_CANCELED:
                Log.d("MainActivity","Cancelled new list creation");
                break;
        }
    }

    private void updateLists(){
        File file=new File(getFilesDir()+"/checkLists.txt");
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(file, false));
            String output = "";
            for (CheckList checkList: checkLists){
                output += checkList.getName()+"\n";
            }
            writer.write(output);
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error creating new checklist", Toast.LENGTH_SHORT).show();
        }
        checklistCollectionPageAdapter.setData(checkLists);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_deletelist) {
            int index = viewPager.getCurrentItem();
            Log.d("MainActivity","Deleted list: '"+checkLists.get(index).getName()+"'");
            // Remove corresponding checklist file
            File file = new File(getFilesDir()+"/"+checkLists.get(index).getName().replace(' ','_')+".txt");
            file.delete();

            // Remove checklist from list
            checkLists.remove(index);

            // Update checklists.txt and UI
            updateLists();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ChecklistCollectionPageAdapter extends FragmentStatePagerAdapter {

        private ArrayList<CheckList> checkLists;

        public ChecklistCollectionPageAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.checkLists = new ArrayList<CheckList>();
        }

        public ChecklistCollectionPageAdapter(@NonNull FragmentManager fm, ArrayList<CheckList> checkLists) {
            super(fm);
            this.checkLists = (ArrayList<CheckList>) checkLists.clone();
        }

        public void setData(ArrayList<CheckList> checkLists){
            this.checkLists.clear();
            this.checkLists = (ArrayList<CheckList>) checkLists.clone();
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return ListFragment.newInstance(this.checkLists.get(position)); //
        }

        @Override
        public int getCount() {
            return checkLists.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.checkLists.get(position).getName();
        }

        @Override
        public int getItemPosition(Object object) {
            ListFragment lf = (ListFragment) object;
            String name = lf.getChecklistName().toString();
            for (int i=0; i< checkLists.size(); ++i){
                if(checkLists.get(i).getName().compareTo(name)==0){
                    return i;
                }
            }
            return POSITION_NONE; // Allow viewpager to delete fragment view and show next available
        }

    }
}
