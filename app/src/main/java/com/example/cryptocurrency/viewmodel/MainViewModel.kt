package com.example.cryptocurrency.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.repository.ApiRepository
import com.example.cryptocurrency.response.ResponseCoinsMarkets
import com.example.cryptocurrency.response.ResponseDetailsCoin
import com.example.cryptocurrency.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    /**
     * List of Coins
     */

    private val _coinsList = MutableLiveData<DataStatus<List<ResponseCoinsMarkets.ResponseCoinsMarketsItem>>>()
    val coinsList: LiveData<DataStatus<List<ResponseCoinsMarkets.ResponseCoinsMarketsItem>>>
        get() = _coinsList


    fun getCoinsList(vs_currency: String) = viewModelScope.launch {
        repository.getCoinsList(vs_currency).collect{
            _coinsList.value =it
        }
    }


    /**
     * Details Coin
     */
    private val _detailsCoin = MutableLiveData<DataStatus<ResponseDetailsCoin>>()
    val detailsCoin: LiveData<DataStatus<ResponseDetailsCoin>>
        get() = _detailsCoin

    fun getDetailsCoin(id: String) = viewModelScope.launch {
        repository.getDetailsCoin(id).collect{
            _detailsCoin.value=it
        }
    }



}