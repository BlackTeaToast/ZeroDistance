package com.yoyoyee.zerodistance.activity;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

        import com.yoyoyee.zerodistance.R;
        import com.yoyoyee.zerodistance.app.QAndA;

public class QAActivity extends AppCompatActivity {
    Button GO;
    QAndA Q =new QAndA();
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
    }
}
