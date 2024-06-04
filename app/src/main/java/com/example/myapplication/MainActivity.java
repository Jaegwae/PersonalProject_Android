package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int j = 0;  //0부터 크롤링한 데이터 값 넣기 (페이지 변화해도 그 번호 그대로 올라갈 수 있도록) 또한 J값만큼 Bundle안에 데이터가 들어가있다고 판단 가능
    List<LP> lpList = new ArrayList<>();
    ListView listview;
    ListAdapter adapter;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.listview = findViewById(R.id.listview);
        radioGroup = findViewById(R.id.radioGroup);
        EditText input_title = (EditText)findViewById(R.id.input_title);
        Button button_search = (Button)findViewById(R.id.button_search);

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lpList.clear();
                String text = input_title.getText().toString();
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            for(int i = 0; i < 3; i++){
                                if(i == 0){
                                    //알라딘
                                    Document doc = Jsoup.connect("https://www.aladin.co.kr/search/wsearchresult.aspx?SearchTarget=Music&SearchWord="+text).get();
                                    lpList.add(
                                            new LP(doc.select(".bo3").get(0).text(),
                                                    doc.select(".ss_p2").get(0).text()
                                            )
                                    );
                                }
                                else if(i == 1){
                                    Document doc = Jsoup.connect("https://www.yes24.com/Product/Search?qdomain=음반&query="+text+"&domain=MUSIC&dispNo2=003001033").get();
                                    lpList.add(
                                            new LP(doc.select(".gd_name").get(0).text(),
                                                    doc.select(".yes_b").get(0).text()
                                            )
                                    );
                                }
                                else{
                                    Document doc = Jsoup.connect("https://gimbabrecords.com/product/search.html?view_type=&supplier_code=&category_no=25&keyword="+text+"&x=0&y=0").get();
                                    lpList.add(
                                            new LP(doc.select(".name").get(0).text(),
                                                    doc.select(".xans-element-.xans-search.xans-search-listitem").get(0).text()
                                            )
                                    );
                                }
                            }

                            adapter = new ListAdapter(lpList);

                            runOnUiThread(() -> {
                                listview.setAdapter(adapter);
                            });


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.aladin){
                    lpList.clear();
                    j=0;
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                //알라딘
                                for(int i = 1; i < 3; i++){
                                    Document doc = Jsoup.connect("https://www.aladin.co.kr/shop/wbrowse.aspx?BrowseTarget=List&ViewRowsCount=25&ViewType=Detail&PublishMonth=0&SortOrder=6&page="
                                            +i+
                                            "&Stockstatus=1&PublishDay=84&CID=86800&SearchOption=&CustReviewRankStart=&CustReviewRankEnd=&CustReviewCountStart=&CustReviewCountEnd=&PriceFilterMin=&PriceFilterMax=&MusicFilter=").get();
                                    for(; j < i*25; j++) {
                                        if(i==1){
                                            lpList.add(
                                                    new LP(doc.select(".bo3").get(j).text(),
                                                            doc.select(".ss_p2").get(j).text()
                                                    )
                                            );
                                        }
                                        else{
                                            lpList.add(
                                                    new LP(doc.select(".bo3").get(j-((i-1)*25)).text(),
                                                            doc.select(".ss_p2").get(j-((i-1)*25)).text()
                                                    )
                                            );
                                        }
                                    }
                                }

                                adapter = new ListAdapter(lpList);

                                runOnUiThread(() -> {
                                    listview.setAdapter(adapter);
                                });


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                else if (checkedId == R.id.yes24){
                    lpList.clear();
                    j=0;
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                //예스24
                                for(int i = 1; i < 3; i++){
                                    Document doc = Jsoup.connect("https://www.yes24.com/24/Category/Display/003001033001?ParamSortTp=02&PageNumber="+i).get();
                                    for(; j < i*20; j++) {
                                        if(i==1){
                                            lpList.add(
                                                    new LP(doc.select(".goods_name").get(j).text(),
                                                            doc.select(".goods_price").get(j).text()
                                                    )
                                            );
                                        }
                                        else{
                                            lpList.add(
                                                    new LP(doc.select(".goods_name").get(j-((i-1)*20)).text(),
                                                            doc.select(".goods_price").get(j-((i-1)*20)).text()
                                                    )
                                            );
                                        }
                                    }
                                }
                                adapter = new ListAdapter(lpList);

                                runOnUiThread(() -> {
                                    listview.setAdapter(adapter);
                                });


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                else {
                    lpList.clear();
                    j=0;
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                //김밥레코즈
                                for(int i = 1; i < 3; i++){
                                    Document doc = Jsoup.connect("https://gimbabrecords.com/product/list.html?cate_no=52&page="+i).get();

                                    for(; j < i*12; j++) {  //첫번째 데이터 이상한 값이 들어감
                                        if(i==1){
                                            lpList.add(
                                                    new LP(doc.select(".inner_L").get(j+1).text(),
                                                            doc.select(".inner_R.price").get(j).text()
                                                    )
                                            );
                                        }
                                        else{
                                            lpList.add(
                                                    new LP(doc.select(".inner_L").get(j-((i-1)*12)+1).text(),
                                                            doc.select(".inner_R.price").get(j-((i-1)*12)).text()
                                                    )
                                            );
                                        }
                                    }
                                }
                                adapter = new ListAdapter(lpList);

                                runOnUiThread(() -> {
                                    listview.setAdapter(adapter);
                                });


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });
    }

}

