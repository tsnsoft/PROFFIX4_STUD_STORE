package kz.proffix4.psunewsreader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public ArrayList<HashMap<String, String>> list;
    CustomListAdapter adapter;
    float fSize;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int poslistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.listView);

        listView.smoothScrollToPosition(4);

        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new NewThread(MainActivity.this).execute();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                poslistView = pos;

                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                HashMap<String, String> result = list.get(pos);

                intent.putExtra("newsURL", result.get("readmore"));
                intent.putExtra("title", result.get("title"));

                startActivity(intent);
            }
        });

        new NewThread(MainActivity.this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_update) {
            new NewThread(MainActivity.this).execute();
            return true;
        }

        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        fSize = Float.parseFloat(prefs.getString(getString(R.string.pref_size), "12"));
        if (listView != null & adapter != null) {
            adapter = new CustomListAdapter(getApplicationContext(), list, fSize);
            listView.setAdapter(adapter);
            if (poslistView != -1) {
                listView.setSelection(poslistView);
            }
        }
    }

    private static class NewThread extends AsyncTask<String, Void, String> {

        private static final String URL = "https://tou.edu.kz/ru/component/news";

        private WeakReference<MainActivity> activityWeakReference;
        MainActivity activity;

        ProgressDialog dialog;

        NewThread(MainActivity context) {
            activityWeakReference = new WeakReference<>(context);
            activity = activityWeakReference.get();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (activity == null || activity.isFinishing()) {
                return;
            }

            dialog = new ProgressDialog(activityWeakReference.get());
            dialog.setProgress(0);
            dialog.setMessage("Обновление..");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg) {
            if (activity == null) {
                return null;
            }

            try {
                activityWeakReference.get().list = new ArrayList<>();
                Document doc = Jsoup.connect(URL).get();
                Element container = doc.selectFirst("div.row > div.col.s12 > div.news-list");
                Elements news = container.select("div.row");
                for (Element row : news) {
                    HashMap<String, String> map = new HashMap<>();
                    Element title = row.selectFirst("div.news-list-title");
                    map.put("title", title.selectFirst("a").text());
                    Element desc = row.selectFirst("div.news-list-introtext");
                    map.put("desc", desc.text());
                    Element image = row.selectFirst("div.news-list-image").selectFirst("img");
                    map.put("image", image.attr("abs:src"));
                    String link = title.selectFirst("a").attr("abs:href");
                    map.put("readmore", link);
                    activityWeakReference.get().list.add(map);
                }
            } catch (Exception ignored) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (activity == null || activity.isFinishing()) {
                return;
            }

            if (dialog.isShowing())
                dialog.dismiss();

            activityWeakReference.get().adapter = new CustomListAdapter(activityWeakReference.get(),
                    activityWeakReference.get().list, activityWeakReference.get().fSize);
            activityWeakReference.get().listView.setAdapter(activityWeakReference.get().adapter);

            if (activityWeakReference.get().list.size() == 0) {
                Toast.makeText(activityWeakReference.get(), "Нет данных", Toast.LENGTH_LONG).show();
            }
        }
    }
}
