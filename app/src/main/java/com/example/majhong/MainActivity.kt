package com.example.majhong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.majhong.database.MajhongDatabase
import com.example.majhong.ui.MainScreen
import com.example.majhong.ui.MainToolBar
import com.example.majhong.ui.theme.MainColor
import com.example.majhong.ui.theme.MajhongTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext, MajhongDatabase::class.java, "player.db"
        )
            .allowMainThreadQueries()
//            .fallbackToDestructiveMigration()
            .build()
    }

    @Suppress("UNCHECKED_CAST")
    private val majhongViewModel by viewModels<MajhongViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MajhongViewModel(db.playerDao, db.majhongDao) as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            db.clearAllTables()
            MajhongTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.background(MainColor)
                    ) {
                        MainToolBar()
                        MainScreen(majhongViewModel)
                    }
                }
            }
        }
    }
}