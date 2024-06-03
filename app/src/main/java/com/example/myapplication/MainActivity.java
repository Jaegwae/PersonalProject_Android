package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {
    String nums;                    //복권 번호을 저장할 변수
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.number);
        final Bundle bundle = new Bundle();

        new Thread(){
            @Override
            public void run() {
                try {
                    for(int i = 1; i < 3; i++){
                        Document doc = Jsoup.connect("https://www.aladin.co.kr/shop/wbrowse.aspx?BrowseTarget=List&ViewRowsCount=25&ViewType=Detail&PublishMonth=0&SortOrder=6&page="+i+"&Stockstatus=1&PublishDay=84&CID=86800&SearchOption=&CustReviewRankStart=&CustReviewRankEnd=&CustReviewCountStart=&CustReviewCountEnd=&PriceFilterMin=&PriceFilterMax=&MusicFilter=").get();

                        for(int j = 0; j < 25; j++) {
                            if(i==1 & j==0){    //첫번째 값 NULL값 없애기 위해
                                Elements contents = doc.select(".bo3");
                                nums = contents.get(j).text();
                                contents = doc.select(".ss_p2");
                                nums += "   "+ contents.get(j).text();
                            }
                            else {
                                Elements contents = doc.select(".bo3");
                                nums += "\n\n" + contents.get(j).text();
                                contents = doc.select(".ss_p2");
                                nums += "   "+ contents.get(j).text();
                            }
                        }
                    }


                    bundle.putString("numbers", nums);                               //핸들러를 이용해서 Thread()에서 가져온 데이터를 메인 쓰레드에 보내준다.
                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            textView.setText(bundle.getString("numbers"));                      //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다.
        }
    };
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
