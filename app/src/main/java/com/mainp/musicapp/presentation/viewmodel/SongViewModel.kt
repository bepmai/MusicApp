package com.mainp.musicapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.data.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SongViewModel(private val repository: SongRepository) : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>(listOf())
    val songs: LiveData<List<Song>> get() = _songs

//    fun getAllSongApi() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _songs.postValue(repository.fetchSongs())
//        }
//    }
    fun getAllSongApi() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val songsList = repository.fetchSongs()
                _songs.postValue(songsList)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Lỗi khi gọi API: ${e.message}")
            }
        }
    }
}

class SongViewModelFactory(
    private val repository: SongRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SongViewModel(repository) as T
    }
}