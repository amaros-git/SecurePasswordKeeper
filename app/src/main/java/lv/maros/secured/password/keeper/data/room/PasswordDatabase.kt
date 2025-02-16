package lv.maros.secured.password.keeper.data.room

//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import lv.maros.secured.password.keeper.models.PasswordDTO
//
//@Database(entities = [PasswordDTO::class], version = 1, exportSchema = false)
//abstract class PasswordDatabase : RoomDatabase() {
//
//    abstract val passwordDao: PasswordDao
//
//    companion object {
//
//        private const val DATABASE_NAME = "passwords.db"
//
//        @Volatile
//        private var INSTANCE: PasswordDatabase? = null
//
//        fun getInstance(context: Context): PasswordDatabase {
//            synchronized(this) {
//                var instance = INSTANCE
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        PasswordDatabase::class.java,
//                        DATABASE_NAME
//                    )
//                        .fallbackToDestructiveMigration()
//                        .build()
//
//                    INSTANCE = instance
//                }
//
//                return instance
//            }
//        }
//
//    }
//
//}