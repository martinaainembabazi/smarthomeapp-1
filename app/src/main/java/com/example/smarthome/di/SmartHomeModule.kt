package com.example.smarthome.di
//import android.app.Application
//import androidx.room.Room
//import com.example.smarthome.data.dao.RoutineDao
//import com.example.smarthome.data.database.AppDatabase
//import com.example.smarthome.data.repository.RoutineRepository
//import com.example.smarthome.ui.viewModel.RoutineViewModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.SupervisorJob
//import org.koin.android.ext.koin.androidApplication
//import org.koin.androidx.viewmodel.dsl.viewModel
//import org.koin.dsl.module
//
//val smartHomeModule = module {
//    single { provideDatabase(androidApplication(), get()) }
//    single { provideRoutineDao(get()) }
//    single { provideCoroutineScope() }
//    single { provideRoutineRepository(get(), get()) }
//    viewModel { RoutineViewModel(get()) }
//}
//
//fun provideDatabase(app: Application, scope: CoroutineScope): AppDatabase {
//    return AppDatabase.getDatabase(app, scope)
//}
//
//fun provideRoutineDao(db: AppDatabase): RoutineDao {
//    return db.routineDao()
//}
//
//fun provideCoroutineScope(): CoroutineScope {
//    return CoroutineScope(SupervisorJob())
//}
//
//fun provideRoutineRepository(routineDao: RoutineDao, app: Application): RoutineRepository {
//    return RoutineRepository(routineDao, app)
//}