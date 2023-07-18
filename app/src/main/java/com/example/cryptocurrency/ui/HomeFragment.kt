package com.example.cryptocurrency.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency.adapter.CryptosAdapter
import com.example.cryptocurrency.databinding.FragmentHomeBinding
import com.example.cryptocurrency.utils.DataStatus
import com.example.cryptocurrency.utils.initRecycler
import com.example.cryptocurrency.utils.isVisible
import com.example.cryptocurrency.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var cryptosAdapter: CryptosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        lifecycleScope.launch {

            binding.apply {
                viewModel.getCoinsList("usd")
                viewModel.coinsList.observe(viewLifecycleOwner) {
                    when (it.status) {
                        DataStatus.Status.LOADING -> {
                            pBarLoading.isVisible(true, rvCrypto)
                        }

                        DataStatus.Status.SUCCESS -> {
                            pBarLoading.isVisible(false, rvCrypto)
                            cryptosAdapter.differ.submitList(it.data)
                            cryptosAdapter.setOnItemClickListener { item ->
                                Log.d("HomeFragment", item.id!!)
                                val direction =
                                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item.id)
                                findNavController().navigate(direction)
                            }
                        }

                        DataStatus.Status.ERROR -> {
                            pBarLoading.isVisible(true, rvCrypto)
                            Toast.makeText(
                                requireContext(),
                                "There is something wrong!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvCrypto.initRecycler(LinearLayoutManager(requireContext()), cryptosAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
