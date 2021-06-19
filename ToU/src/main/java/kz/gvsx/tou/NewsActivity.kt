package kz.gvsx.tou

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kz.gvsx.tou.databinding.ActivityNewsBinding

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityNewsBinding.inflate(layoutInflater).root)
    }
}