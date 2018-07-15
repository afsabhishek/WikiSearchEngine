package com.android.wikimedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.android.wikimedia.model.ResponseDto;
import com.android.wikimedia.model.Pages;
import com.android.wikimedia.network.NetworkAPI;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity implements NetworkAPI.FetchSearchListInterface{
    ListView listView;
    TextView textView;
    SearchView searchView;
    ArrayList<Pages> searchArrayList ;
    ResultAdapter resultAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        searchView = findViewById(R.id.searchView);

        searchList();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    protected void searchList(){
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String inputQuery) {
                if (inputQuery.length() > 3)
                {
                   NetworkAPI.getSearchResult(SearchActivity.this,inputQuery);
                }else if(inputQuery.length() == 0 ){
                    if(searchArrayList != null)
                        searchArrayList.clear();
                    if(resultAdapter != null){
                        listView.setAdapter(null);
                        resultAdapter.notifyDataSetChanged();
                    }
                }
                return false;
            }

        });
    }



    @Override
    public void fetchSearchImp(ResponseDto results) {
        if(results != null){
            searchArrayList = new ArrayList<Pages>(Arrays.asList(results.getQuery().getPages()));
            ResultAdapter resultAdapter = new ResultAdapter(searchArrayList,SearchActivity.this);
            listView.setAdapter(resultAdapter);
        }
    }
}
