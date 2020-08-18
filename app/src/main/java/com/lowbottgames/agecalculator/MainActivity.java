package com.lowbottgames.agecalculator;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.lowbottgames.agecalculator.adapter.AgeListAdapter;
import com.lowbottgames.agecalculator.dialog.InputDialogFragment;
import com.lowbottgames.agecalculator.util.DataHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        InputDialogFragment.DPFOnDateSetListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    RecyclerView recyclerView;
    AgeListAdapter ageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempFixForCoordinatorLayoutBug();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);

        setSupportActionBar(toolbar);
        collapsingToolbar.setTitle(getString(R.string.app_name));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<PersonModel> personList = DataHelper.getAllPersonModel(this);

        ageListAdapter = new AgeListAdapter(personList, new AgeListAdapter.AgeListAdapterListener(){
            @Override
            public void onItemClick(PersonModel personModel) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);

                intent.putExtra(InfoActivity.KEY_ID, personModel.getId());
                intent.putExtra(InfoActivity.KEY_NAME, personModel.getName());
                intent.putExtra(InfoActivity.KEY_DAY, personModel.getDay());
                intent.putExtra(InfoActivity.KEY_MONTH, personModel.getMonth() + 1);
                intent.putExtra(InfoActivity.KEY_YEAR, personModel.getYear());

//                startActivity(intent);
                startActivityForResult(intent, InfoActivity.REQUEST_CODE);
            }
        });
        recyclerView.setAdapter(ageListAdapter);

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputFragment();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == InfoActivity.REQUEST_CODE){
            reloadAgeListAdapter();
        }

    }

    void showInputFragment(){
        InputDialogFragment fragment = InputDialogFragment.newInstance();
        fragment.show(getSupportFragmentManager(), "InputDialogFragment");
    }

    void reloadAgeListAdapter(){
        ageListAdapter.setPersonList(DataHelper.getAllPersonModel(this));
    }

    void tempFixForCoordinatorLayoutBug(){
        ViewGroup appBarLayout = (ViewGroup) findViewById(R.id.appBarLayout);
        for (int i = 0; i < appBarLayout.getChildCount(); i++) {
            View childView = appBarLayout.getChildAt(i);
            if (!childView.isClickable()) {
                childView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_person) {
            showInputFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onInputSet(String name, int year, int month, int day) {
        Log.e(TAG, "year = " + year + "  month = " + month + "  day = " + day);
        PersonModel personModel = new PersonModel();
        personModel.setName(name);
        personModel.setYear(year);
        personModel.setMonth(month);
        personModel.setDay(day);

        DataHelper.inserOrUpdate(this, personModel);
        reloadAgeListAdapter();
    }
}
