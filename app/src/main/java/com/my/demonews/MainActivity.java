package com.my.demonews;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.my.demonews.domain.NewInfo;
import com.my.demonews.utils.ViewHolder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private ListView mListNews;
    private final int SUCCESS = 0;
    private final int FAILED = 1;
    List<NewInfo> newsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mListNews = (ListView) findViewById(R.id.listNews);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NewInfo> newsList = getNewsListForInternet();
                Message msg = new Message();
                if (newsList != null) {
                    msg.what = SUCCESS;
                    msg.obj = newsList;
                } else {
                    msg.what = FAILED;
                }
                mHandler.sendMessage(msg);
            }

        }).start();
    }

    /**
     * Get Data For Internet
     */
    public List<NewInfo> getNewsListForInternet() {
        HttpClient client = null;
        try {
            client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://10.0.2.2:8080/tp/");
            HttpResponse response = client.execute(get);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                InputStream is = response.getEntity().getContent();
                List<NewInfo> data = getNewsListFromInputStream(is);
                return data;
            } else {
                Log.i("myinfo", "访问失败" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
        }
        return null;
    }

    private List<NewInfo> getNewsListFromInputStream(InputStream is) throws Exception {
        Type listType = new TypeToken<ArrayList<NewInfo>>() {
        }.getType();
        byte[] data = IOUtils.read(is);
        String jsonStr = new String(data);
        Gson gson = new Gson();
        List<NewInfo> newsList = gson.fromJson(jsonStr, listType);
        return newsList;
    }

    public static class IOUtils {
        /**
         * 读取输入流为byte[]数组
         */
        public static byte[] read(InputStream instream) throws Exception {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = instream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    newsInfo = (List<NewInfo>) msg.obj;
                    MyAdapter myAdapter = new MyAdapter();
                    mListNews.setAdapter(myAdapter);
                    break;
                case FAILED:
                    Toast.makeText(MainActivity.this, "NetWork Failed", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    };

    class MyAdapter extends BaseAdapter {

        private Context mContext = MainActivity.this;
        private LayoutInflater mInflater;

        MyAdapter() {
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return newsInfo.size();
        }

        @Override
        public Object getItem(int i) {
            return newsInfo.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = mInflater.inflate(R.layout.item, null);
            }
            TextView title = ViewHolder.get(view, R.id.title);
            TextView detail = ViewHolder.get(view, R.id.detail);
            title.setText(newsInfo.get(i).getTitle());
            detail.setText(newsInfo.get(i).getDetail());
            return view;
        }
    }
}