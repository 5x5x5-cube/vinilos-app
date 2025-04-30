package com.uniandes.vinilosapp

import android.app.Application
import com.uniandes.vinilosapp.database.VinilosDatabase


class VinilosApplication: Application()  {

    val database by lazy { VinilosDatabase.getDatabase(this) }
}