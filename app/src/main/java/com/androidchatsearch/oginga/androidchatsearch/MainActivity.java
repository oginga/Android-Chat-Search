package com.androidchatsearch.oginga.androidchatsearch;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<String> chatConvo = new ArrayList<String>();
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    //Search
    private ArrayList<Integer> foundIndices;
    private  int currentPos;
    private String searchQuery;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setting up the TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Chat Screen");

        //Layout setup
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());

        //Setup recycleview adapter
        chatAdapter=new ChatAdapter(this);

        recyclerView = (RecyclerView) findViewById(R.id.chatRecycler);
        this.recyclerView.setLayoutManager(mLinearLayoutManager);
        this.recyclerView.setAdapter(this.chatAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        LinearLayoutCompat.LayoutParams navButtonsParams = new LinearLayoutCompat.LayoutParams(toolbar.getHeight() * 2 / 3, toolbar.getHeight() * 2 / 3);

        Button btnDown = new Button(this);
        btnDown.setBackground(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));

        Button btnUp = new Button(this);
        btnUp.setBackground(getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));

        TextView searchStats = new TextView(this);

        ((LinearLayout) searchView.getChildAt(0)).addView(searchStats);
        ((LinearLayout) searchView.getChildAt(0)).addView(btnUp, navButtonsParams);
        ((LinearLayout) searchView.getChildAt(0)).addView(btnDown, navButtonsParams);

        ((LinearLayout) searchView.getChildAt(0)).setGravity(Gravity.BOTTOM);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemsVisibility(menu, searchItem, false);
            }
        });
        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setItemsVisibility(menu, searchItem, true);
                if (foundIndices!=null)
                    foundIndices.clear();
                chatAdapter.notifyDataSetChanged();

                return false;
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                currentPos=0;
                return false;
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchQuery.equals("")) {
//                    chatAdapter.notifyDataSetChanged();
                    final String sq=searchQuery;
                    if (foundIndices==null  || foundIndices.isEmpty()){
                        foundIndices=chatAdapter.filter(searchQuery);
                        Collections.reverse(foundIndices);
                    }
//                    foundIndices=chatAdapter.filter(searchQuery);
                    if (!foundIndices.isEmpty()){
                        switch (currentPos){
                            case 0:
                                Toast.makeText(getApplicationContext(),"Search upwards",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                final int pos=currentPos;

                                final int itemPos=foundIndices.get(pos);

                                recyclerView.scrollToPosition(itemPos);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        View found = mLinearLayoutManager.findViewByPosition(itemPos);
                                        RelativeLayout foundLay = (RelativeLayout) found;

                                        if (foundLay!=null){

                                            String toSearch;
                                            TextView thisView;

                                            TextView cust= (TextView) foundLay.getChildAt(0);

                                            String in=cust.getText().toString().toLowerCase();

                                            TextView cust1= (TextView) foundLay.getChildAt(1);

                                            String out=cust1.getText().toString().toLowerCase();

                                            if (!in.isEmpty()) {
                                                toSearch=in;
                                                thisView=cust;
                                            }else {
                                                toSearch=out;
                                                thisView=cust1;
                                            }

                                            if (toSearch!=null && thisView!=null) {

                                                int ofe = toSearch.indexOf(sq.toLowerCase(),0);
                                                Spannable WordtoSpan = new SpannableString( thisView.getText() );

                                                for(int ofs=0;ofs<toSearch.length() && ofe!=-1;ofs=ofe+1)
                                                {
                                                    ofe = toSearch.indexOf(sq. toLowerCase(),ofs);
                                                    if(ofe == -1)
                                                        break;
                                                    else
                                                    {
                                                        WordtoSpan.setSpan(new BackgroundColorSpan(0xFFFFFF00), ofe, ofe+searchQuery.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                        thisView.setText(WordtoSpan, TextView.BufferType.SPANNABLE);
                                                    }
                                                }
                                            }

                                        }


                                    }
                                }, 500);
                                currentPos-=1;
                                currentPos-=1;
                        }


                    }else {
                        Toast.makeText(getApplicationContext(),"No match found!",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Type sth",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchQuery.equals("")) {

                    final String sq=searchQuery;
                    if (foundIndices==null  || foundIndices.isEmpty()){
                        foundIndices=chatAdapter.filter(searchQuery);
                        Collections.reverse(foundIndices);
                    }
                    if (!foundIndices.isEmpty()){
                        int foundSize=foundIndices.size()-1;

                        if (currentPos>foundSize){
                            Toast.makeText(getApplicationContext(),"Search downwards",Toast.LENGTH_SHORT).show();
                            currentPos-=1;
                        }else if(currentPos<foundSize){
                            final int pos=currentPos;
                            final int itemPos=foundIndices.get(pos);

                            recyclerView.scrollToPosition(itemPos);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    View found = mLinearLayoutManager.findViewByPosition(itemPos);
                                    RelativeLayout foundLay = (RelativeLayout) found;

                                    if (foundLay!=null){
                                        String toSearch;
                                        TextView thisView;

                                        TextView cust= (TextView) foundLay.getChildAt(0);

                                        String in=cust.getText().toString().toLowerCase();
                                        TextView cust1= (TextView) foundLay.getChildAt(1);

                                        String out=cust1.getText().toString().toLowerCase();

                                        if (!in.isEmpty()) {
                                            toSearch=in;
                                            thisView=cust;
                                        }else {
                                            toSearch=out;
                                            thisView=cust1;
                                        }

                                        if (toSearch!=null && thisView!=null) {

                                            int ofe = toSearch.indexOf(sq.toLowerCase(),0);
                                            Spannable WordtoSpan = new SpannableString( thisView.getText() );

                                            for(int ofs=0;ofs<toSearch.length() && ofe!=-1;ofs=ofe+1)
                                            {
                                                ofe = toSearch.indexOf(sq. toLowerCase(),ofs);
                                                if(ofe == -1)
                                                    break;
                                                else
                                                {
                                                    WordtoSpan.setSpan(new BackgroundColorSpan(0xFFFFFF00), ofe, ofe+searchQuery.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                    thisView.setText(WordtoSpan, TextView.BufferType.SPANNABLE);
                                                }
                                            }
                                        }
                                    }


                                }
                            }, 500);
                            currentPos+=1;

                        }


                    }else {
                        Toast.makeText(getApplicationContext(),"No match found!",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Type sth", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return true;
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }
}
