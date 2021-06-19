package kz.gvsx.tou

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kz.gvsx.tou.databinding.FragmentHomeViewPagerBinding
import kz.gvsx.tou.ui.news.NewsFragment
import kz.gvsx.tou.ui.notifications.NotificationsFragment

private const val NUM_PAGES = 2

@AndroidEntryPoint
class HomeViewPagerFragment : Fragment() {

    private var _binding: FragmentHomeViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeViewPagerBinding.inflate(inflater, container, false)

        binding.viewPager.adapter = PagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

        val tabPositionToTitle =
            mapOf(0 to getString(R.string.tab_news), 1 to getString(R.string.tab_notifications))

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabPositionToTitle[position]
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class PagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragment, lifecycle) {
        override fun getItemCount(): Int = NUM_PAGES
        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> NewsFragment()
            1 -> NotificationsFragment()
            else -> throw IndexOutOfBoundsException("Not valid position for HomeViewPagerFragment.PagerAdapter.createFragment()")
        }
    }
}