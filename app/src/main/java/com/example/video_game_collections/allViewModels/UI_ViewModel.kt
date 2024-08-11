package com.example.video_game_collections.allViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UI_ViewModel : ViewModel() {


    private var _addProductDialogueCardState  = MutableLiveData<Boolean>()
    var addProductDialogueCardState : LiveData<Boolean> = _addProductDialogueCardState





    fun makeAddProductDialogueCardHidden(){
        _addProductDialogueCardState.value = false
    }

    fun makeAddProductDialogueCardVisible(){
        _addProductDialogueCardState.value = true
    }


}