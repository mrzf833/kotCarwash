package org.d3if3090.carwash.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class CarwashDb: RoomDatabase() {
    abstract val historyDao: HistoryDao

    companion object{
        @Volatile
        private var INSTANCE: CarwashDb? = null
        fun getInstance(context: Context): CarwashDb {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CarwashDb::class.java,
                        "carwash.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return  instance
            }
        }
    }
}