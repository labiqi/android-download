package com.example.lcq.drawable.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lcq.drawable.Utils.ThreadInfo;

import java.util.ArrayList;
import java.util.List;


//数据访问接口实现类

public class ThreadDaoImp implements ThreadDao {

    private DBHelper dbHelper = null;

    public ThreadDaoImp(Context context) {
//        dbHelper = new DBHelper(context);
        dbHelper = DBHelper.getInstance(context);
    }

    @Override
    public synchronized void insertThread(ThreadInfo threadInfo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into thread_info(thread_id,url,start,end,finished) values (?,?,?,?,?)",
                new Object[]{threadInfo.getId(), threadInfo.getUrl(), threadInfo.getStart(), threadInfo.getEnd(), threadInfo.getFinished()});
        db.close();
    }

    @Override
    public synchronized void deleteThread(String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from thread_info where url = ?",
                new Object[]{url});
        db.close();
    }

    @Override
    public synchronized void updateThread(String url, int thread_id, int finished) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update  thread_info set finished = ? where url = ? and thread_id = ?",
                new Object[]{finished,url, thread_id});
        db.close();
    }

    @Override
    public List<ThreadInfo> getThreads(String url) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<ThreadInfo> list = new ArrayList<ThreadInfo>();
       Cursor cursor = db.rawQuery("select * from thread_info where url = ?",new String[]{url});
       while(cursor.moveToNext()) {
           ThreadInfo threadInfo = new ThreadInfo();
           threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
           threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
           threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
           threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
           threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
           list.add(threadInfo);
       }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public boolean isexit(String url, int thread_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url = ? and thread_id = ?",new String[]{url,thread_id+""});
        boolean exit = cursor.moveToNext();
        cursor.close();
        db.close();
        return exit;
    }
}
