package kz.proffix4.psunewsreader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    CustomListAdapter adapter;

    public ArrayList<HashMap<String, String>> list;

    float fSize;
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
                new NewThread().execute();
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

        new NewThread().execute();
    }

    public class NewThread extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... arg) {
            try {
                list = new ArrayList<>();
                String URL = "http://psu.kz/index.php?lang=rus";
                Document doc = Jsoup.connect(URL).get();
                Elements containers = doc.select("div.moduletable");
                Element container = containers.get(0);
                Elements news = container.select("div.slide");
                for (Element row : news.select("div.bt-row")) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    Elements inner = row.select("div.bt-inner");
                    Elements title = inner.select("a.bt-title");
                    map.put("title", title.text());
                    Elements descs = row.select("div.bt-introtext");
                    Element desc = descs.get(0);
                    map.put("desc", desc.text());
                    Elements images = row.select("div.bt-center a.bt-image-link img");
                    String imgSrcUrl = images.attr("abs:src");
                    map.put("image", imgSrcUrl);
                    Element readmore = row.select("p.readmore a").first();
                    String link = readmore.attr("abs:href");
                    map.put("readmore", link);
                    list.add(map);
                }
            } catch (Exception ignored) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgress(0);
            dialog.setMessage("Обновление..");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog.isShowing())
                dialog.dismiss();

            adapter = new CustomListAdapter(getApplicationContext(), list, fSize);
            listView.setAdapter(adapter);

            if (list.size() == 0) {
                Toast.makeText(getApplicationContext(), "Нет данных", Toast.LENGTH_LONG).show();
            }
        }
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
            new NewThread().execute();
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
}
