package kz.gvsx.tou.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kz.gvsx.tou.databinding.FragmentNotificationsBinding

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationsViewModel by viewModels()

    private val notificationsAdapter = NotificationsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            swipeRefresh.isRefreshing = true
            swipeRefresh.setOnRefreshListener {
                viewModel.fetchNotifications()
            }
            recyclerView.adapter = notificationsAdapter
        }


        viewModel.notifications.observe(viewLifecycleOwner, { notifications ->
            notificationsAdapter.submitList(notifications)
            binding.swipeRefresh.isRefreshing = false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }
}