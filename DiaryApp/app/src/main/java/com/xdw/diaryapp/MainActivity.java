package com.xdw.diaryapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.media.CamcorderProfile.get;

public class MainActivity extends Activity {
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView=findViewById(R.id.list1);
        registerForContextMenu(listView);  //注册上下文菜单

        dbhelper=new DatabaseHelper(this);  //创建SQLiteOpenHelper对象

        final ArrayList<Map<String,String>> items=getAll();  //全部的记录显示到list中
        setDiariesListView(items);
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),CheckActivity.class);
                intent.putExtra("ids",items.get(position).get(id));
                startActivity(intent);
                MainActivity.this.finish();
            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbhelper.close();
    }


    private void setDiariesListView(ArrayList<Map<String,String>> items){  //设置适配器，显示列表
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,items,R.layout.item,
                new String[]{
                        Diaries.Diary._ID,
                        Diaries.Diary.COLUMN_NAME_DATE,
                        Diaries.Diary.COLUMN_NAME_WEATHER,
                        Diaries.Diary.COLUMN_NAME_CONTENT},
                new int[]{
                        R.id.textViewId,
                        R.id.textViewDate,
                        R.id.textViewWeather,
                        R.id.textViewContent});
        ListView listView=findViewById(R.id.list1);
        listView.setAdapter(simpleAdapter);
    }

    private ArrayList<Map<String,String>> getAll(){
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        String[] projection={
                Diaries.Diary._ID,
                Diaries.Diary.COLUMN_NAME_DATE,
                Diaries.Diary.COLUMN_NAME_WEATHER,
                Diaries.Diary.COLUMN_NAME_CONTENT,

        };
        String sortOrder=Diaries.Diary.COLUMN_NAME_DATE+" DESC"; //按照日期排序
        Cursor c=db.query(
                Diaries.Diary.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        return ConvertCursorList(c);
    }

    private ArrayList<Map<String,String>> ConvertCursorList(Cursor cursor){
        ArrayList<Map<String,String>> result=new ArrayList<>();
        while(cursor.moveToNext()){
            Map<String,String> map=new HashMap<>(); //哈希表
            map.put(Diaries.Diary._ID,String.valueOf(cursor.getInt(0)));
            map.put(Diaries.Diary.COLUMN_NAME_DATE,cursor.getString(1));
            map.put(Diaries.Diary.COLUMN_NAME_WEATHER,cursor.getString(2));
            map.put(Diaries.Diary.COLUMN_NAME_CONTENT,cursor.getString(3));//返回当前行指定列的值
            result.add(map);
        }
        return result;
    }


    public boolean onCreateOptionsMenu(Menu menu){  //选项菜单
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){  //右上角选项
        int id=item.getItemId();
        switch(id){
            case R.id.action_search:
                SearchDialog();
                return true;
            case R.id.action_insert:
                InsertDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View view,ContextMenu.ContextMenuInfo menuInfo){  //上下文菜单
        getMenuInflater().inflate(R.menu.contextmenu_diarieslistview,menu);
    }

    public boolean onContextItemSelected(MenuItem item){
        TextView textId;
        TextView textDate;
        TextView textWeather;
        TextView textContent;

        AdapterView.AdapterContextMenuInfo info;
        View itemView;

        switch (item.getItemId()){
            case R.id.action_delete:  //删除
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId=itemView.findViewById(R.id.textViewId);
                if(textId!=null){
                    String strId=textId.getText().toString();
                    DeleteDialog(strId);
                }
                break;
            case R.id.action_update:  //修改
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId=itemView.findViewById(R.id.textViewId);
                textDate=itemView.findViewById(R.id.textViewDate);
                textWeather=itemView.findViewById(R.id.textViewWeather);
                textContent=itemView.findViewById(R.id.textViewContent);
                if(textId!=null&&textDate!=null&&textWeather!=null&&textContent!=null){
                    String strId=textId.getText().toString();
                    String strDate=textDate.getText().toString();
                    String strWeather=textWeather.getText().toString();
                    String strContent=textContent.getText().toString();
                    UpdateDialog(strId,strDate,strWeather,strContent);
                }
                break;
            case R.id.action_check:
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId=itemView.findViewById(R.id.textViewId);
                textDate=itemView.findViewById(R.id.textViewDate);
                textWeather=itemView.findViewById(R.id.textViewWeather);
                textContent=itemView.findViewById(R.id.textViewContent);
                if(textId!=null&&textDate!=null&&textWeather!=null&&textContent!=null){
                    String strId=textId.getText().toString();
                    String strDate=textDate.getText().toString();
                    String strWeather=textWeather.getText().toString();
                    String strContent=textContent.getText().toString();
                    CheckDialog(strId,strDate,strWeather,strContent);
                }
                break;
        }
        return true;
    }

    private void Insert(String strDate,String strWeather,String strContent){  //增
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        ContentValues values=new ContentValues();  //键值对
        values.put(Diaries.Diary.COLUMN_NAME_DATE,strDate);
        values.put(Diaries.Diary.COLUMN_NAME_WEATHER,strWeather);
        values.put(Diaries.Diary.COLUMN_NAME_CONTENT,strContent);
        long newRowId=db.insert(  //插入数据库新的内容
                Diaries.Diary.TABLE_NAME,
                null,
                values
        );
    }

    private void InsertDialog(){
        final RelativeLayout relativeLayout=(RelativeLayout)getLayoutInflater().inflate(R.layout.insert,null);  //将xml布局实例化
        new AlertDialog.Builder(this)  //dialog的一个子类
                .setTitle("新增日记")
                .setView(relativeLayout)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strDate=((EditText)relativeLayout.findViewById(R.id.insertDate)).getText().toString();
                        String strWeather=((EditText)relativeLayout.findViewById(R.id.insertWeather)).getText().toString();
                        String strContent=((EditText)relativeLayout.findViewById(R.id.insertContent)).getText().toString();
                        Insert(strDate,strWeather,strContent);
                        ArrayList<Map<String,String>> items=getAll();
                        setDiariesListView(items);
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void Delete(String strId){  //删
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        String selection=Diaries.Diary._ID+"=?";  //定义where子句
        String[] selectionArgs={strId};  //指定占位符对应的实际参数
        db.delete(Diaries.Diary.TABLE_NAME,selection,selectionArgs);
    }

    private void DeleteDialog(final String strId){
        new AlertDialog.Builder(this)
                .setTitle("删除日记")
                .setMessage("是否确定删除")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Delete(strId);
                        setDiariesListView(getAll());
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void Update(String strId,String strDate,String strWeather,String strContent){  //改
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        ContentValues values=new ContentValues();  //键值对
        values.put(Diaries.Diary.COLUMN_NAME_DATE,strDate);
        values.put(Diaries.Diary.COLUMN_NAME_WEATHER,strWeather);
        values.put(Diaries.Diary.COLUMN_NAME_CONTENT,strContent);
        String selection=Diaries.Diary._ID+"=?";
        String[] selectionArgs={strId};
        int count=db.update(
                Diaries.Diary.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    private void UpdateDialog(final String strId,final String strDate,final String strWeather,final String strContent){
        final RelativeLayout relativeLayout=(RelativeLayout)getLayoutInflater().inflate(R.layout.insert,null);
        ((EditText)relativeLayout.findViewById(R.id.insertDate)).setText(strDate);
        ((EditText)relativeLayout.findViewById(R.id.insertWeather)).setText(strWeather);
        ((EditText)relativeLayout.findViewById(R.id.insertContent)).setText(strContent);
        new AlertDialog.Builder(this)
                .setTitle("修改日记")
                .setView(relativeLayout)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strNewDate=((EditText)relativeLayout.findViewById(R.id.insertDate)).getText().toString();
                        String strNewWeather=((EditText)relativeLayout.findViewById(R.id.insertWeather)).getText().toString();
                        String strNewContent=((EditText)relativeLayout.findViewById(R.id.insertContent)).getText().toString();
                        Update(strId,strNewDate,strNewWeather,strNewContent);
                        setDiariesListView(getAll());
                    }
                })
                .create()
                .show();
    }


    private void CheckDialog(final String strId,final String strDate,final String strWeather,final String strContent){
        final RelativeLayout relativeLayout=(RelativeLayout) getLayoutInflater().inflate(R.layout.activity_check,null);
        ((TextView)relativeLayout.findViewById(R.id.checkDate)).setText(strDate);
        ((TextView)relativeLayout.findViewById(R.id.checkDate)).setCursorVisible(false);
        (relativeLayout.findViewById(R.id.checkDate)).setFocusable(false);
        (relativeLayout.findViewById(R.id.checkDate)).setFocusableInTouchMode(false);
        ((TextView)relativeLayout.findViewById(R.id.checkWeather)).setText(strWeather);
        ((TextView)relativeLayout.findViewById(R.id.checkWeather)).setCursorVisible(false);
        (relativeLayout.findViewById(R.id.checkWeather)).setFocusable(false);
        (relativeLayout.findViewById(R.id.checkWeather)).setFocusableInTouchMode(false);
        ((TextView)relativeLayout.findViewById(R.id.checkContent)).setText(strContent);
        ((TextView)relativeLayout.findViewById(R.id.checkContent)).setCursorVisible(false);
        (relativeLayout.findViewById(R.id.checkContent)).setFocusable(false);
        (relativeLayout.findViewById(R.id.checkContent)).setFocusableInTouchMode(false);
        new AlertDialog.Builder(this)
                .setTitle("查看日记")
                .setView(relativeLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private ArrayList<Map<String,String>> Search(String strDiarySearch){  //查
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        String[] projection={
                Diaries.Diary._ID,
                Diaries.Diary.COLUMN_NAME_DATE,
                Diaries.Diary.COLUMN_NAME_WEATHER,
                Diaries.Diary.COLUMN_NAME_CONTENT
        };
        String sortOrder=Diaries.Diary.COLUMN_NAME_DATE+" DESC";  //列表排序
        String selection= Diaries.Diary.COLUMN_NAME_CONTENT+" LIKE?";
        String[] selectionArgs={"%"+strDiarySearch+"%"}; //内容模糊查询
        Cursor c=db.query(
                Diaries.Diary.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return ConvertCursorList(c);
    }

    private void SearchDialog(){
        final RelativeLayout relativeLayout=(RelativeLayout)getLayoutInflater().inflate(R.layout.search,null);  //将xml布局实例化
        new AlertDialog.Builder(this)  //dialog的一个子类
                .setTitle("查找日记")
                .setView(relativeLayout)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strSearchDiary=((EditText)relativeLayout.findViewById(R.id.txtSearchContent)).getText().toString();
                        ArrayList<Map<String,String>> items;
                        items=Search(strSearchDiary);
                        if(items.size()>0){
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("result",items);
                            Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,"该日记不存在",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
        Log.v("tag","search");
    }
}
