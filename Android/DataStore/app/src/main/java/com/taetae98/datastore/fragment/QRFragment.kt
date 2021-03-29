package com.taetae98.datastore.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.taetae98.datastore.R
import com.taetae98.datastore.base.BaseFragment
import com.taetae98.datastore.databinding.FragmentQrBinding
import com.taetae98.datastore.singleton.Account
import com.taetae98.datastore.singleton.LoginDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class QRFragment : BaseFragment<FragmentQrBinding>(R.layout.fragment_qr) {
    init {
        setHasOptionsMenu(true)
    }

    @Inject
    lateinit var account: Account

    @Inject
    lateinit var loginDataStore: LoginDataStore

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_qr_option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                loginDataStore.id = ""
                loginDataStore.password = ""
                findNavController().navigateUp()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        onCreateSupportActionBar()
        onCreateOnRefresh()
        onCreateRefreshCoroutine()
        return view
    }

    private fun onCreateSupportActionBar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun onCreateOnRefresh() {
        binding.setOnRefresh {
            refresh()
        }
    }

    private fun onCreateRefreshCoroutine() {
        lifecycleScope.launchWhenResumed {
            while (true) {
                refresh()
                delay(10000L)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun refresh() {
        binding.qrCode = "m${account.studentId}${SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())}"
        binding.lastUpdated = "Last Updated : ${SimpleDateFormat.getTimeInstance().format(System.currentTimeMillis())}"
    }
}