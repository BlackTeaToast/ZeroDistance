package com.yoyoyee.zerodistance.activity;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.Button;

        import com.yoyoyee.zerodistance.R;
        import com.yoyoyee.zerodistance.app.QAndA;
        import com.yoyoyee.zerodistance.helper.QAAdapter;

public class QAActivity extends AppCompatActivity {
    Button GO;
    QAndA Q =new QAndA();
    Boolean isQ=true;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);
        GO = (Button)findViewById(R.id.for_Q_Button);
        GO.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QAActivity.this, AskActivity.class);
                startActivity(intent);
            }
        });
        //listview start
        String[] q_Q_Titletext = {"滾", "你有病嘛", "閃邊", "87", "P0", "好啦好啦"},a_A_Titletext = {"我難過", "打屁屁", "878787", "打屁屁", "878787", "打屁屁"};
        String[] q_Qtimetext = {"1/11 1:11", "12/11 11:11", "xx", "48/43", "154/45", "12/12"}, q_Qnametext = {"我難過", "打屁屁", "878787", "打屁屁", "878787", "打屁屁"};
        String[] q_Qcontenttext = {"你說把愛漸漸放下會走更遠,或許命運的謙讓我遇見", "你好阿", "xx", "你好阿", "xx", "你好阿"}, a_Atimetext = {"我難過", "打屁屁", "878787", "打屁屁", "878787", "打屁屁"};
        String[] a_Acontenttext = {"滾", "你有病嘛", "閃邊", "87", "P0", "好啦好啦"}, a_Anametext = {"978", "978", "978", "978", "978", "978"};

            QAAdapter QAAdapter = new QAAdapter(q_Q_Titletext, q_Qtimetext, q_Qnametext, q_Qcontenttext, a_A_Titletext, a_Atimetext, a_Acontenttext, a_Anametext);
            RecyclerView mList = (RecyclerView) findViewById(R.id.QAlistView);

            LinearLayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mList.setLayoutManager(layoutManager);
            mList.setAdapter(QAAdapter);


        //listview end
    }

}
