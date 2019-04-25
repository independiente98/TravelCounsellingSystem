package com.example.travelcounsellingsystem;
import java.util.*;
import android.*;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import questionnaire.Answer;
import questionnaire.Page;
import questionnaire.Question;

import static android.Manifest.permission.READ_CONTACTS;

public class QustionnaireActivity extends Activity{
    private LinearLayout test_layout;
    private Page the_page;
    //答案列表
    private ArrayList<Answer> the_answer_list;
    //问题列表
    private ArrayList<Question> the_question_list;
    //问题所在的View
    private View que_view;
    //答案所在的View
    private View ans_view;
    private LayoutInflater xInflater;
    private Page page;
    //下面这两个list是为了实现点击的时候改变图片，因为单选多选时情况不一样，为了方便控制
    //存每个问题下的imageview
    private ArrayList<ArrayList<ImageView>> imglist=new ArrayList<ArrayList<ImageView>>();
    //存每个答案的imageview
    private ArrayList<ImageView> imglist2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_layout);
        xInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //假数据
        initDate();
        //提交按钮
        Button button= findViewById(R.id.submit);
        button.setOnClickListener(new submitOnClickListener(page));
    }
    private void initDate() {
        //假数据
        // TODO Auto-generated method stub
        Answer a_one=new Answer();
        a_one.setAnswerId("0");
        a_one.setAnswer_content("YES");
        a_one.setAns_state(0);
        Answer a_two=new Answer();
        a_two.setAnswerId("1");
        a_two.setAnswer_content("NO");
        a_two.setAns_state(0);

        Answer a_three=new Answer();
        a_three.setAnswerId("3");
        a_three.setAnswer_content("Natural scenery");
        a_three.setAns_state(0);
        Answer a_four=new Answer();
        a_four.setAnswerId("4");
        a_four.setAnswer_content("Human landscape");
        a_four.setAns_state(0);



        Answer a_five=new Answer();
        a_five.setAnswerId("5");
        a_five.setAnswer_content("Entertainment places");
        a_five.setAns_state(0);


        ArrayList<Answer> answers_one=new ArrayList<Answer>();
        answers_one.add(a_one);
        answers_one.add(a_two);


        ArrayList<Answer> answers_two=new ArrayList<Answer>();
        answers_two.add(a_three);
        answers_two.add(a_four);
        answers_two.add(a_five);


        Question q_one=new Question();
        q_one.setQuestionId("00");
        q_one.setType("0");
        q_one.setContent("Do you prefer a long journey?");
        q_one.setAnswers(answers_one);
        q_one.setQue_state(0);

        Question q_two=new Question();
        q_two.setQuestionId("01");
        q_two.setType("1");
        q_two.setContent("Which of the following types of city you prefer？");
        q_two.setAnswers(answers_two);
        q_two.setQue_state(0);



        ArrayList<Question> questions=new ArrayList<Question>();
        questions.add(q_one);
        questions.add(q_two);

        page=new Page();
        page.setPageId("000");
        page.setStatus("0");
        page.setTitle("User Preference");
        page.setQuestions(questions);
        //加载布局
        initView(page);
    }
    private void initView(Page page) {
        // TODO Auto-generated method stub
        //这是要把问题的动态布局加入的布局
        test_layout= findViewById(R.id.test);
        TextView page_txt= findViewById(R.id.txt_title);
        page_txt.setText(page.getTitle());
        //获得问题即第二层的数据
        the_question_list=page.getQuestions();
        //根据第二层问题的多少，来动态加载布局
        for(int i=0;i<the_question_list.size();i++){
            que_view=xInflater.inflate(R.layout.question_layout, null);
            TextView txt_que= que_view.findViewById(R.id.question_item);
            //这是第三层布局要加入的地方
            LinearLayout add_layout= que_view.findViewById(R.id.answer);
            //判断单选-多选来实现后面是*号还是*多选，
            if(the_question_list.get(i).getType().equals("1")){
                set(txt_que,the_question_list.get(i).getContent(),1);
            }else{
                set(txt_que,the_question_list.get(i).getContent(),0);
            }
            //获得答案即第三层数据
            the_answer_list=the_question_list.get(i).getAnswers();
            imglist2=new ArrayList<ImageView>();
            for(int j=0;j<the_answer_list.size();j++){
                ans_view=xInflater.inflate(R.layout.answer_layout, null);
                TextView txt_ans= ans_view.findViewById(R.id.answer_item);
                ImageView image= ans_view.findViewById(R.id.image);
                View line_view=ans_view.findViewById(R.id.vw_line);
                if(j==the_answer_list.size()-1){
                    //最后一条答案下面不要线是指布局的问题
                    line_view.setVisibility(View.GONE);
                }
                //判断单选多选加载不同选项图片
                if(the_question_list.get(i).getType().equals("1")){
                    image.setBackgroundDrawable(getResources().getDrawable(R.drawable.square1));
                }else{
                    image.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio));
                }
                Log.e("---", "------"+image);
                imglist2.add(image);
                txt_ans.setText(the_answer_list.get(j).getAnswer_content());
                LinearLayout lly_answer_size= ans_view.findViewById(R.id.answer_size);
                lly_answer_size.setOnClickListener(new answerItemOnClickListener(i,j,the_answer_list,txt_ans));
                add_layout.addView(ans_view);
            }
			/*for(int r=0; r<imglist2.size();r++){
				Log.e("---", "imglist2--------"+imglist2.get(r));
			}*/

            imglist.add(imglist2);

            test_layout.addView(que_view);
        }
		/*for(int q=0;q<imglist.size();q++){
			for(int w=0;w<imglist.get(q).size();w++){
				Log.e("---", "共有------"+imglist.get(q).get(w));
			}
		}*/

    }
    private void set(TextView tv_test, String content,int type) {
        //为了加载问题后面的* 和*多选
        // TODO Auto-generated method stub
        String w;
        if(type==1){
            w = content+"*[多选题]";
        }else{
            w = content+"*";
        }

        int start = content.length();
        int end = w.length();
        Spannable word = new SpannableString(w);
        word.setSpan(new AbsoluteSizeSpan(25), start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        word.setSpan(new StyleSpan(Typeface.BOLD), start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        word.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv_test.setText(word);
    }
    class answerItemOnClickListener implements OnClickListener{
        private int i;
        private int j;
        private TextView txt;
        private ArrayList<Answer> the_answer_lists;
        public answerItemOnClickListener(int i,int j, ArrayList<Answer> the_answer_list,TextView text){
            this.i=i;
            this.j=j;
            this.the_answer_lists=the_answer_list;
            this.txt=text;

        }
        //实现点击选项后改变选中状态以及对应图片
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //判断当前问题是单选还是多选
			/*Log.e("------", "选择了-----第"+i+"题");
			for(int q=0;q<imglist.size();q++){
				for(int w=0;w<imglist.get(q).size();w++){
//					Log.e("---", "共有------"+imglist.get(q).get(w));
				}
			}
			Log.e("----", "点击了---"+imglist.get(i).get(j));*/

            if(the_question_list.get(i).getType().equals("1")){
                //多选
                if(the_answer_lists.get(j).getAns_state()==0){
                    //如果未被选中
                    txt.setTextColor(Color.parseColor("#EA5514"));
                    imglist.get(i).get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.tick));
                    the_answer_lists.get(j).setAns_state(1);
                    the_question_list.get(i).setQue_state(1);
                }else{
                    txt.setTextColor(Color.parseColor("#595757"));
                    imglist.get(i).get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.square1));
                    the_answer_lists.get(j).setAns_state(0);
                    the_question_list.get(i).setQue_state(1);
                }
            }else{
                //单选

                for(int z=0;z<the_answer_lists.size();z++){
                    the_answer_lists.get(z).setAns_state(0);
                    imglist.get(i).get(z).setBackgroundDrawable(getResources().getDrawable(R.drawable.radio));
                }
                if(the_answer_lists.get(j).getAns_state()==0){
                    //如果当前未被选中
                    imglist.get(i).get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.circletick));
                    the_answer_lists.get(j).setAns_state(1);
                    the_question_list.get(i).setQue_state(1);
                }else{
                    //如果当前已被选中
                    the_answer_lists.get(j).setAns_state(1);
                    the_question_list.get(i).setQue_state(1);
                }

            }
            //判断当前选项是否选中



        }

    }
    class submitOnClickListener implements OnClickListener{
        private Page page;
        public submitOnClickListener(Page page){
            this.page=page;
        }
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //判断是否答完题
            boolean isState=true;
            //最终要的json数组
            JSONArray jsonArray = new JSONArray();
            //点击提交的时候，先判断状态，如果有未答完的就提示，如果没有再把每条答案提交（包含问卷ID 问题ID 及答案ID）
            //注：不用管是否是一个问题的答案，就以答案的个数为准来提交上述格式的数据
            for(int i=0;i<the_question_list.size();i++){
                the_answer_list=the_question_list.get(i).getAnswers();
                //判断是否有题没答完
                if(the_question_list.get(i).getQue_state()==0){
                    Toast.makeText(getApplicationContext(), "您第"+(i+1)+"题没有答完", Toast.LENGTH_LONG).show();
                    jsonArray=null;
                    isState=false;
                    break;
                }else{
                    for(int j=0;j<the_answer_list.size();j++){
                        if(the_answer_list.get(j).getAns_state()==1){
                            JSONObject json = new JSONObject();
                            try {
                                json.put("psychologicalId", page.getPageId());
                                json.put("questionId", the_question_list.get(i).getQuestionId());
                                json.put("optionId", the_answer_list.get(j).getAnswerId());
                                jsonArray.put(json);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
            if(isState){
                if(jsonArray.length()>0){
                    Log.e("af", jsonArray.toString());
                    for(int item=0;item<jsonArray.length();item++){
                        JSONObject job;
                        try {
                            job = jsonArray.getJSONObject(item);
                            Log.e("----", "pageId--------"+job.get("pageId"));
                            Log.e("----", "questionId--------"+job.get("questionId"));
                            Log.e("----", "answerId--------"+job.get("answerId"));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }  // 遍历 jsonarray 数组，把每一个对象转成 json 对象

                    }

                }

            }

        }
    }
}
