package com.example.hsguang.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

import bsh.EvalError;
import bsh.Interpreter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText = null;  //显示器
    boolean isClear = false;  //用于是否显示器需要被清理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        Button btn_sqr = (Button)findViewById(R.id.btn_sqr);
        Button btn_x2 = (Button)findViewById(R.id.btn_x2);
        Button btn_ce = (Button)findViewById(R.id.btn_ce);
        Button btn_sin = (Button)findViewById(R.id.btn_sin);
        Button btn_cos = (Button)findViewById(R.id.btn_cos);
        Button btn_tan = (Button)findViewById(R.id.btn_tan);

        Button btn_add = (Button)findViewById(R.id.btn_add);
        Button btn_div = (Button)findViewById(R.id.btn_div);
        Button btn_sub = (Button)findViewById(R.id.btn_sub);
        Button btn_equ = (Button)findViewById(R.id.btn_equ);
        Button btn_mul = (Button)findViewById(R.id.btn_mul);
        Button btn_dot = (Button)findViewById(R.id.btn_dot);

        Button btn_c = (Button)findViewById(R.id.btn_c);
        Button btn_0 = (Button)findViewById(R.id.btn_0);
        Button btn_1 = (Button)findViewById(R.id.btn_1);
        Button btn_2 = (Button)findViewById(R.id.btn_2);
        Button btn_3 = (Button)findViewById(R.id.btn_3);
        Button btn_4 = (Button)findViewById(R.id.btn_4);
        Button btn_5 = (Button)findViewById(R.id.btn_5);
        Button btn_6 = (Button)findViewById(R.id.btn_6);
        Button btn_7 = (Button)findViewById(R.id.btn_7);
        Button btn_8 = (Button)findViewById(R.id.btn_8);
        Button btn_9 = (Button)findViewById(R.id.btn_9);

        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_sub.setOnClickListener(this);
        btn_div.setOnClickListener(this);
        btn_equ.setOnClickListener(this);
        btn_dot.setOnClickListener(this);
        btn_mul.setOnClickListener(this);
        btn_sqr.setOnClickListener(this);
        btn_sin.setOnClickListener(this);
        btn_x2.setOnClickListener(this);
        btn_ce.setOnClickListener(this);
        btn_c.setOnClickListener(this);
        btn_cos.setOnClickListener(this);
        btn_tan.setOnClickListener(this);
    }

    @Override
    public void onClick(View e) {

        Button btn = (Button) e;
        String exp = editText.getText().toString();
        if(isClear &&(
                btn.getText().equals("0")
                        ||btn.getText().equals("1")
                        ||btn.getText().equals("2")
                        ||btn.getText().equals("3")
                        ||btn.getText().equals("4")
                        ||btn.getText().equals("5")
                        ||btn.getText().equals("6")
                        ||btn.getText().equals("7")
                        ||btn.getText().equals("8")
                        ||btn.getText().equals("9")
                        ||btn.getText().equals("."))
                ||btn.getText().equals("算数公式错误")){
            editText.setText("");
            isClear = false;
        }
        if (btn.getText().equals("c")){
            editText.setText("");
        }else if(btn.getText().equals("清除")){
            if(isEmpty(exp)) return;
            else
                editText.setText(exp.substring(0,exp.length()-1));
        }

        //实现sin，cos，开根，幂函数
        else if(btn.getText().equals("sqr(")){
            TextView sart=(TextView) findViewById(R.id.editText);
            double a=Double.parseDouble(sart.getText().toString());
            double num1=Math.sqrt(a);
            editText.setText(Double.toString(num1));
        }
        else if(btn.getText().equals("sin(")){
            TextView sart=(TextView) findViewById(R.id.editText);
            double a=Double.parseDouble(sart.getText().toString());
            double num2=Math.sin(a*Math.PI/180);
            editText.setText(Double.toString(num2));
        }
        else if(btn.getText().equals("cos(")){
            TextView sart=(TextView) findViewById(R.id.editText);
            double a=Double.parseDouble(sart.getText().toString());
            double num3=Math.cos(a*Math.PI/180);
            editText.setText(Double.toString(num3));
        }

        else if(btn.getText().equals("tan(")){
            TextView sart=(TextView) findViewById(R.id.editText);
            double a=Double.parseDouble(sart.getText().toString());
            double num4=Math.tan(a*Math.PI/180);
            editText.setText(Double.toString(num4));
        }

        else if(btn.getText().equals("X^2")){
            TextView sart=(TextView) findViewById(R.id.editText);
            double a=Double.parseDouble(sart.getText().toString());
            double num5=a*a;
            editText.setText(Double.toString(num5));
        }


        else if(btn.getText().equals("=")){
            if(isEmpty(exp)) return;
            exp = exp.replaceAll("×", "*");
            exp = exp.replaceAll("÷", "/");
            editText.setText(getRs(exp));
            isClear = false;
        }

        else{
            editText.setText(editText.getText()+""+btn.getText());
            isClear = false;
        }
        //操作完成后始终保持光标在最后一位
        editText.setSelection(editText.getText().length());
    }


    /* param  exp 算数表达式
     return 根据表达式返回结果
    */
    private String getRs(String exp){
        Interpreter bsh = new Interpreter();
        Number result = null;
        try {
            exp = filterExp(exp);
            result = (Number)bsh.eval(exp);
        } catch (EvalError e) {
            e.printStackTrace();
            isClear = true;
            return "算数公式错误";
        }
        exp = result.doubleValue()+"";
        if(exp.endsWith(".0"))
            exp = exp.substring(0, exp.indexOf(".0"));
        return exp;
    }



    /* 因为计算过程中,全程需要有小数参与,所以需要过滤一下
    param exp 算数表达式
    return
    */
    private String filterExp(String exp) {
        String num[] = exp.split("");
        String temp = null;
        int begin=0,end=0;
        for (int i = 1; i < num.length; i++) {
            temp = num[i];
            if(temp.matches("[+-/()*]")){
                if(temp.equals(".")) continue;
                end = i - 1;
                temp = exp.substring(begin, end);
                if(temp.trim().length() > 0 && temp.indexOf(".")<0)
                    num[i-1] = num[i-1]+".0";
                begin = end + 1;
            }
        }
        return Arrays.toString(num).replaceAll("[\\[\\], ]", "");
    }


    /* param str
    * return 字符串非空验证
    */
    private boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    }

}