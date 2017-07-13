package com.moruna.asynctasktest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncTaskTest extends AppCompatActivity {
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task_test);
        dialog = new ProgressDialog(AsyncTaskTest.this);
        dialog.setTitle("download test asynctask");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
    }

    public void click(View view) {
        new DownloadTask().execute("http://pic129.nipic.com/file/20170516/20614752_221848813000_2.jpg");
    }

    /**
     * 1. Params 在执行AsyncTask时需要传入的参数，可用于在后台任务中使用
     * 2. Progress 后台任务执行时，如果需要在界面上显示当前的进度，则使用这里指定的泛型作为进度单位
     * 3. Result 当任务执行完毕后，如果需要对结果进行返回，则使用这里指定的泛型作为返回值类型
     */
    private class DownloadTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            URL url;
            HttpURLConnection connection;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
                int contentLength;
                if (connection.getResponseCode() == 200) {
                    contentLength = connection.getContentLength();
                    InputStream inputStream = connection.getInputStream();
                    File file = new File(getCacheDir(), "test.jpg");
                    FileOutputStream outputStream = new FileOutputStream(file);
                    int len;
                    long totalSize = 0;
                    byte[] bytes = new byte[1024];
                    while ((len = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, len);
                        totalSize += len * 1024;
                        publishProgress((int) (totalSize / contentLength));
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss();
            if (aBoolean) {
                Toast.makeText(getApplicationContext(), "download success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "download fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
