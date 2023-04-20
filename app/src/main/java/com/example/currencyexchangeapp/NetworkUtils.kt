package com.example.currencyexchangeapp

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

object NetworkUtils {

    interface NetworkStatusListener {
        fun onNetworkStatusChanged(isConnected: Boolean)
    }

    private var networkStatusListener: NetworkStatusListener? = null
    private var connectivityManager: ConnectivityManager? = null

    private val networkCallbacks = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            networkStatusListener?.onNetworkStatusChanged(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            networkStatusListener?.onNetworkStatusChanged(false)
        }
    }

    fun registerNetworkStatusListener(context: Context, listener: NetworkStatusListener) {
        networkStatusListener = listener
        if (connectivityManager == null) connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallbacks)
    }

    fun unregisterNetworkStatusListener() {
        networkStatusListener = null
        connectivityManager?.unregisterNetworkCallback(networkCallbacks)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        if (connectivityManager == null) connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager?.activeNetwork
        val networkCapabilities = connectivityManager?.getNetworkCapabilities(network)
        val withInternet = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        if (!withInternet) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("No Network")
            builder.setMessage("Please check your network connection and try again.")
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog?.show()
            return false
        }
        return true
    }

}