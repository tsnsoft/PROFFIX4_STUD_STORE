package kz.proffix4.psunewsreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    private String URL;

    private TextView titleTextView, desc1TextView, desc2TextView;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();// возврат на предыдущий activity
            }
        });

        URL = getIntent().getStringExtra("newsURL");
        title = getIntent().getStringExtra("title");

        titleTextView = findViewById(R.id.title);
        desc1TextView = findViewById(R.id.desc1);
        desc2TextView = findViewById(R.id.desc2);
        new NewThread().execute();
    }

    public class NewThread extends AsyncTask<String, Void, String> {
        String titleText, desc1Text;
        StringBuilder desc2Text;
        int size;
        ArrayList<String> imagelinks;

        @Override
        protected String doInBackground(String... arg) {
            try {
                Document doc = Jsoup.connect(URL).get();
                Elements container = doc.select("div.post-inner");

                try {
                    Element title = container.select("h2.postheader").first();
                    titleText = title.text();
                } catch (Exception e) {
                    if (title != null) titleText = title;
                    else titleText = "";
                }


                Elements content = container.select("div.postcontent div.article");
                Elements desc = content.select("p[style]");
                Element header = desc.first();
                desc1Text = header.text();
                desc.remove(header);

                desc2Text = new StringBuilder();
                for (Element desc2 : desc) {
                    desc2Text.append(desc2.text() + "\n");
                }

                Element imagelist = content.select("ul.imagelist").first();
                imagelinks = new ArrayList<String>();
                for (Element image : imagelist.select("li img")) {
                    imagelinks.add(image.attr("abs:src"));
                }
                size = imagelinks.size();

            } catch (Exception ignored) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                titleTextView.setText(titleText);
                desc1TextView.setText(desc1Text);

                RecyclerView rv = findViewById(R.id.rv);
                if (size == 1) {
                    StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                    rv.setLayoutManager(sglm);
                } else {
                    StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    rv.setLayoutManager(sglm);
                }

                ImageGridAdapter iga = new ImageGridAdapter(getApplicationContext(), imagelinks);
                rv.setAdapter(iga);

                desc2TextView = findViewById(R.id.desc2);
                desc2TextView.setText(desc2Text);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
        float fSize = Float.parseFloat(prefs.getString(getString(R.string.pref_size), "14"));

        desc1TextView.setTextSize(fSize);
        desc2TextView.setTextSize(fSize);
    }

}
