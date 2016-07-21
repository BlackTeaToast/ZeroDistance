package com.yoyoyee.zerodistance.menuDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.SessionFunctions;

/**
 * Created by 楊霖村 on 2016/7/9.
 */
public class Dialog_myself extends Dialog {
    Button btn_confirm;
    Button btn_exit;
    TextView name, profession, level, exp, Achievement;
    EditText Aboutmyself;
    boolean beonclick=false;
    public Dialog_myself(Context context) {
       super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_personal_page);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.personal_page);
        btn_confirm = (Button)findViewById(R.id.edit);
        btn_exit = (Button)findViewById(R.id.exit);
        name = (TextView)findViewById(R.id.name);
        profession  = (TextView)findViewById(R.id.profession);
        level = (TextView)findViewById(R.id.level);
        exp = (TextView)findViewById(R.id.exp);
        Achievement = (TextView)findViewById(R.id.Achievement);
        Aboutmyself = (EditText)findViewById(R.id.Aboutmyself);
        Aboutmyself.setEnabled(false);
//        getWindow().setBackgroundDrawable(new BitmapDrawable());
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ori();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!beonclick){
                    Aboutmyself.setEnabled(true);
                    Toast.makeText(getContext(),"可以編輯自我介紹",Toast.LENGTH_SHORT).show();
                    beonclick=true;
                    btn_confirm.setText(getContext().getResources().getString(R.string.saveandout));
                }else {
                    Aboutmyself.setEnabled(false);
                    Toast.makeText(getContext(),"已送出",Toast.LENGTH_SHORT).show();
                    beonclick=false;
                    btn_confirm.setText(getContext().getResources().getString(R.string.edit_pop));
                }
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"you click cancel!",Toast.LENGTH_SHORT).show();
                cancel();
            }
        });

    }
    private void ori(){
        name.setText(SessionFunctions.getUserName());
        profession.setText("狂郎勇士");
        level.setText("200");
        exp.setText("333");
        Achievement.setText("萬粽之王");
        Aboutmyself.setText("你好，我是楊霖村，我對金融業有絕大的信心和熱忱，因為我擁有專業的金融知識，利用一年時間積極考去取十張證照，在財務個案分析方面也有研究，另外也具有豐富的實習經驗(拿出財富管理競賽作品、實習在職證明給面試官看)\n" +
                "\n" +
                "我的個性是樂於與人相處的，服務客人時遇到不同情況也應對進退得宜，凡事不斤斤計較，做事不拖泥帶水，在學校參與過許多活動，所以與人相處融洽是我能做到的!\n" +
                "\n" +
                "我所推崇的大人物為巴菲特，雖然他不是銀行家，但卻是我很推崇的專業投資人(備用人物:彭淮南、威爾許、張忠謀、李昌鈺、陳嫦芬)\n" +
                "\n" +
                "我會來爭取此次○○機會的原因為，我把自己當作商品一樣來經營，必須要隨時(不斷地)更新、隨時進步，把每一次挑戰都當作是對自己的磨練，這就是我的工作哲學\n" +
                "\n" +
                "有句話說:「當別人逃避，而你留下來挑戰，就能得到更上一層樓的機會。」這句話我覺得很符合○○○○這工作的正面態度，希望進入貴公司可以秉持我的工作哲學以及座右銘面對挑戰，貢獻所學並勝任愉快! ");
        Aboutmyself.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}

