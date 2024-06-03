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
    Bundle nums = new Bundle(); //크롤링 데이터 저장용
    int j = 0;  //0부터 크롤링한 데이터 값 넣기 (페이지 변화해도 그 번호 그대로 올라갈 수 있도록) 또한 J값만큼 데이터가 들어가있다고 판단 가능
    private LinearLayout container; //부모 뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (LinearLayout) findViewById(R.id.layout);   //부모뷰 지정

        new Thread(){
            @Override
            public void run() {
                try {
                    for(int i = 1; i < 3; i++){
                        Document doc = Jsoup.connect("https://www.aladin.co.kr/shop/wbrowse.aspx?BrowseTarget=List&ViewRowsCount=25&ViewType=Detail&PublishMonth=0&SortOrder=6&page="+i+"&Stockstatus=1&PublishDay=84&CID=86800&SearchOption=&CustReviewRankStart=&CustReviewRankEnd=&CustReviewCountStart=&CustReviewCountEnd=&PriceFilterMin=&PriceFilterMax=&MusicFilter=").get();

                        for(; j < i*25; j++) {
                            if(i==1){
                                Elements contents = doc.select(".bo3");
                                nums.putString("title"+j,contents.get(j).text());
                                contents = doc.select(".ss_p2");
                                nums.putString("value"+j,contents.get(j).text());
                            }
                            else{
                                Elements contents = doc.select(".bo3");
                                nums.putString("title"+j,contents.get(j-((i-1)*25)).text());    //2번째페이지의 첫번째 데이터는 0부터 시작하기 때문에
                                contents = doc.select(".ss_p2");
                                nums.putString("value"+j,contents.get(j-((i-1)*25)).text());    //2번째페이지의 첫번째 데이터는 0부터 시작하기 때문에
                            }
                        }
                    }


                    Message msg = handler.obtainMessage();  //핸들러를 이용해서 Thread()에서 가져온 데이터를 메인 쓰레드에 보내준다.
                    msg.setData(nums);
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
            for(int i = 0; i<j; i++){
                textview(bundle.getString("title"+i));  //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다. // 텍스트 뷰 생성
            }
        }
    };


    public void textview(String a){ //텍스트 뷰 동적생성
        //TextView 생성
        TextView view1 = new TextView(this);
        view1.setText(a);
        view1.setTextSize(12);
        view1.setTextColor(Color.BLACK);

        //layout_width, layout_height, gravity 설정
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.LEFT;
        view1.setLayoutParams(lp);

        //부모 뷰에 추가
        container.addView(view1);
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
