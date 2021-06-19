package kz.gvsx.tou.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kz.gvsx.tou.HomeViewPagerFragmentDirections
import kz.gvsx.tou.R
import kz.gvsx.tou.databinding.FragmentNewsBinding

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news) {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsAdapter = NewsAdapter { news, _ ->
            val action =
                HomeViewPagerFragmentDirections.actionHomeViewPagerFragmentToArticleFragment(
                    news.linkToArticle,
                    news.title
                )
            findNavController().navigate(action)
        }

        with(binding) {
            swipeRefresh.isRefreshing = true
            swipeRefresh.setOnRefreshListener {
                viewModel.fetchNews()
            }
            recyclerView.apply {
                adapter = newsAdapter
                addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            }
        }

        viewModel.news.observe(viewLifecycleOwner, { news ->
            newsAdapter.submitList(news)
            binding.swipeRefresh.isRefreshing = false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }
}