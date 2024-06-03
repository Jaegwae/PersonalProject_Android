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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    private LinearLayout container; //부모 뷰
    List<LP> lpList = new ArrayList<>();
    ListView listview;
    ListAdapter adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.listview = findViewById(R.id.listview);

        new Thread(){
            @Override
            public void run() {
                try {
                    for(int i = 1; i < 3; i++){
                        Document doc = Jsoup.connect("https://www.aladin.co.kr/shop/wbrowse.aspx?BrowseTarget=List&ViewRowsCount=25&ViewType=Detail&PublishMonth=0&SortOrder=6&page="+i+"&Stockstatus=1&PublishDay=84&CID=86800&SearchOption=&CustReviewRankStart=&CustReviewRankEnd=&CustReviewCountStart=&CustReviewCountEnd=&PriceFilterMin=&PriceFilterMax=&MusicFilter=").get();

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



                    adpter = new ListAdapter(lpList);

                    runOnUiThread(() -> {
                        listview.setAdapter(adpter);
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}







//public class MainActivity extends AppCompatActivity {
//    String nums;                    //복권 번호을 저장할 변수
//    TextView textView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        textView = (TextView) findViewById(R.id.number);
//        final Bundle bundle = new Bundle();
//
//        new Thread(){
//            @Override
//            public void run() {
//                Document doc = null;
//                try {
//                    doc = Jsoup.connect("https://dhlottery.co.kr/common.do?method=main").get();
//                    Elements contents = doc.select("#lottoDrwNo");          //회차 id값 가져오기
//                    nums += contents.text() +"회 :";
//
//                    for(int i = 1; i < 7; i++){
//                        contents = doc.select("#drwtNo"+i);                 //복권 번호 6개 가져오기
//                        nums += " "+contents.text();
//                    }
//                    nums += doc.select("#bnusNo").text();                   //보너스 번호 contents 변수를 사용하지 않고 가져오는 방법
//
//                    bundle.putString("numbers", nums);                               //핸들러를 이용해서 Thread()에서 가져온 데이터를 메인 쓰레드에 보내준다.
//                    Message msg = handler.obtainMessage();
//                    msg.setData(bundle);
//                    handler.sendMessage(msg);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            Bundle bundle = msg.getData();
//            textView.setText(bundle.getString("numbers"));                      //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다.
//        }
//    };
//}
