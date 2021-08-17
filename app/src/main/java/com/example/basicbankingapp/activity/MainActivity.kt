package com.example.basicbankingapp.activity

import android.content.Context
import android.content.DialogInterface
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.room.Room
import com.example.basicbankingapp.R
import com.example.basicbankingapp.database.PersonDatabase
import com.example.basicbankingapp.database.PersonEntity
import com.example.basicbankingapp.fragments.AddFragment
import com.example.basicbankingapp.fragments.DashboardFragment
import com.example.basicbankingapp.fragments.TranscationFragment
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setuptoolbar()
        SetUpDashboardFragment()
        val count: Int = DBAsyncTask2(applicationContext).execute().get()
        if(count>0){

        }
        else {
            val personList = listOf<PersonEntity>(
                    PersonEntity(1, "Yugant Gholase", "yugantgholase27@gmail.com", "8876212345", R.drawable.maleuser, "Male", 10000),
                    PersonEntity(2, "Naruto Uzumaki", "UzumakiNaruto@gmail.com", "8856987654", R.drawable.maleuser, "Male", 9000),
                    PersonEntity(3, "Hinata Uzumaki", "UzumakiHinata03@gmail.com", "9422198769", R.drawable.femaleuser, "Female", 8000),
                    PersonEntity(4, "Sakura Uchiha", "sakurauchiha@gmail.com", "7218100620", R.drawable.femaleuser, "Female", 7000),
                    PersonEntity(5, "Sasuke Uchiha", "sasukeuchiha02@gmail.com", "8856972534", R.drawable.maleuser, "Male", 6000),
                    PersonEntity(6, "Boruto Uzumaki", "borutouzumaki2001@gmail.com", "8856982369", R.drawable.maleuser, "Male", 5000),
                    PersonEntity(7, "Sarada Uchiha", "uchihasarada29@gmail.com", "8856982369", R.drawable.femaleuser, "Female", 4000),
                    PersonEntity(8, "Itachi Uchiha", "bestuchiha@gmail.com", "8856982369", R.drawable.maleuser, "Male", 3000),
                    PersonEntity(9, "Minato Namikaze", "minatoNamikaze4@gmail.com", "8856982369", R.drawable.maleuser, "Male", 2000),
                    PersonEntity(10, "Kushina Uzumkai ", "kushinaUzumaki@gmail.com", "8856982369", R.drawable.femaleuser, "Female", 1000),
            )
            val async1 = DBAsyncTask(applicationContext, personList, 1).execute()
        }

        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigation_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.dashboard -> {
                    SetUpDashboardFragment()
                }
                R.id.adduser ->{
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout, AddFragment()).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.history ->{
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout, TranscationFragment()).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.exit -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.dialogTitle)
                    builder.setMessage(R.string.dialogMessage)
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    builder.setPositiveButton("Yes"){ dialogInterface: DialogInterface, i: Int ->
                       ActivityCompat.finishAffinity(this)
                    }
                    builder.setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->
                        drawerLayout.closeDrawers()
                    }
                    builder.setNeutralButton("Cancel"){ dialogInterface: DialogInterface, i: Int ->
                        SetUpDashboardFragment()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setuptoolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title= "Basic Banking App"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id ==  android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun SetUpDashboardFragment(){
        val dashfragment = DashboardFragment()
        supportFragmentManager.beginTransaction().replace(R.id.framelayout, dashfragment).commit()
        drawerLayout.closeDrawers()
        navigation_view.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.framelayout)
        when(fragment){
            !is DashboardFragment -> {
                SetUpDashboardFragment()
            }
            else -> {
                super.onBackPressed()
            }
        }

    }

    @Suppress("DEPRECATION")
    class DBAsyncTask(val context: Context, val personList: List<PersonEntity>, val mode: Int) : AsyncTask<Void, Void, Boolean>(){
        val db= Room.databaseBuilder(context, PersonDatabase::class.java, "person-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                1-> {
                    db.personDao().insertData(personList)
                    return true
                }
            }
            return false
        }

    }

    class DBAsyncTask2(val context: Context) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int {
            val db = Room.databaseBuilder(context, PersonDatabase::class.java, "person-db").build()
            return db.personDao().getCount()
        }
    }

}