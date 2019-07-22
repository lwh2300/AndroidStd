package com.example.androidstd;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidstd.model.User;
import com.example.androidstd.sql.MyDataBaseHelper;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.sql.Connection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBtn1();//sharedPreferences读写
        initBtn2(); //sqllite练习
        initBtn3();//litepal练习
        initBtn4();//权限获取练习
    }

    //权限获取练习
    private void initBtn4() {
        Button btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    call();
                }
            }
        });
    }

    private void call() {
        try {
            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:18520809398"));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(i);
        }catch (Exception e){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         switch (requestCode){
             case 1:
                 if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                     call();
                 }else{
                     Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                 }
                 ;
             default:;
         }
    }

    //litepal练习
    private void initBtn3() {
        Button btn3 =(Button)findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Connector.getDatabase();

                User u=new User();
                u.setName("Lwh");
                u.setAge(11);
                u.save();//类似于jfinal的数据库操作

                //update方法
                u.setName("LWH");
                u.save();//对已保存过对象进行值得修改在保存，注：自处与jfinal不同，不能通过实例化相同主键对象进行值修改
                        //每次的实例化对象进行save都是一条新数据进行insert，id自增长时id的修改无效；

 /*
                u=new User();
                u.setName("hao");
                u.updateAll("id=? or name=?","1","Ln");//通过where给予条件进行修改
                Log.i(TAG,"2:");                                // 第一个参数为where后面的约束条件，
                                                                      // 从第二个参数往后为占位？的值
                u=new User();
                u.setToDefault("age");                                  //将字段还原至默认值得方法
                u.updateAll("id=? or name=?","1","Ln");
                Log.i(TAG,"3:");

                u=new User();
                u.setId(3);
                u.setName("HAO");
                u.save();
                */

                List<User> user=DataSupport.findAll(User.class);
                Log.i(TAG,user.toString());
//                DataSupport.select().where().order().limit().offset().find();   DataSupport有相应的方法进行自定义sql语句查询
                //                 limit（10).offset(5) 等同于sql语句中的limit 5,10;跳过5条显示10条
//                DataSupport.deleteAll("user");
            }
        });
    }

    //sqllite练习
    private void initBtn2() {
        Button btn2 =(Button)findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDataBaseHelper mdbh=new MyDataBaseHelper(MainActivity.this,"mydatabase",null,2);
                Log.i(TAG,mdbh.getDatabaseName());
                SQLiteDatabase db=mdbh.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("name","lwh");
                values.put("tel","13146169164");
                db.insert("user",null,values);
                Cursor cursor =db.query("user",new String[]{"*"},"id=?",new String[]{"1"},null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        Log.i(TAG,"name="+cursor.getString(cursor.getColumnIndex("name")));
                        Log.i(TAG,"tel="+cursor.getString(cursor.getColumnIndex("tel")));
                    }while (cursor.moveToNext());
                }
            }
        });
    }

    //sharedPreferences读写
    private void initBtn1() {
        Button btn1=(Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp=getSharedPreferences("myData",0);
                SharedPreferences.Editor edit=sp.edit();
                edit.putString("name","lwh");
                edit.putInt("age",20);
                edit.putBoolean("is",true);
                edit.apply();

                Log.i("====",getSharedPreferences("myData",0).getAll().toString());
            }
        });
    }

    public void toThreadActivity(View v){
        Intent i=new Intent(this,ThreadActivity.class);
        startActivity(i);
    }
}
