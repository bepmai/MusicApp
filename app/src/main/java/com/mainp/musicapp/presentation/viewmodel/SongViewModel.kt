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

    private val _directSongUrl = MutableLiveData<String?>()
    val directSongUrl: LiveData<String?> get() = _directSongUrl

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

    fun fetchDirectSongUrl(songUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val directUrl = repository.fetchDirectSongUrl(songUrl)
                _directSongUrl.postValue(directUrl)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Lỗi khi lấy link nhạc trực tiếp: ${e.message}")
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