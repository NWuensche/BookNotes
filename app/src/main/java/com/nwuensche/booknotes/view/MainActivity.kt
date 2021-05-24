package com.nwuensche.booknotes.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.nwuensche.booknotes.R
import com.nwuensche.booknotes.model.Book
import com.nwuensche.booknotes.presenter.MainPresenter


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, com.nwuensche.booknotes.view.MenuView {


    private lateinit var presenter: MainPresenter
    var list: ArrayList<MenuItem> = arrayListOf()
    override lateinit var context: Context
    private val notesView: EditText by lazy {findViewById<EditText>(R.id.notesView)}
    private val nav_view: NavigationView by lazy {findViewById<NavigationView>(R.id.nav_view)}
    private val toolbar: Toolbar by lazy {findViewById<Toolbar>(R.id.toolbar)}
    private val drawer_layout: DrawerLayout by lazy {findViewById<DrawerLayout>(R.id.drawer_layout)}

    override fun showBookNotes(title: String, notes: String) {
        runOnUiThread {
            this.title = title
            notesView.text = SpannableStringBuilder(notes)
        }
    }

    override fun updateBookList(books: List<Book>) {
        runOnUiThread {
            //nav_view.menu.clear()

            //TODO Update besser machen
            nav_view.menu.apply {
              /*  val addItemHand = itemsSequence().find { it.title == "Add Book by Hand"}?
                val addItemPhoto = itemsSequence().find { it.title == "Add Book Photo"}?.itemId
                if (addItemHand != null) {
                    clear()
                }
                if (addItemPhoto != null) {
                    add(addItemPhoto)
                }
                add(R.drawable.ic_add_black_24dp, "Add Book by Hand")*/
                //itemsSequence().filter { !it.title.contains("Add") }.map { it.itemId }.forEach { removeItem(it) }
                for(i in list) {
                    this.removeItem(i.itemId)
                }
                list.clear()
                for (i in 0 until books.size) {
                    list.add(add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, books[i].title))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //TODO 1. Buch anzeigen2

        presenter = MainPresenter(this).apply { onCreate() }
        context = applicationContext

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_save -> {
                presenter.updateBook(title = this.title.toString(), notes = notesView.text.toString())
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.title) {
            "Add Book by Hand" -> {
                getAndSaveNewTitle()
            }
            "Add Book by Photo" -> {
                val intent = Intent(applicationContext, ScannerActivity::class.java)
                startActivityForResult(intent, 1337)

            }
            else -> {
                presenter.showBookNotes(item.title.toString())
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1337) {
            if (data!!.hasExtra("ISBN")) {
                presenter.addBook(data.getStringExtra("ISBN"))
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun getAndSaveNewTitle() {
        val dialog = AlertDialog.Builder(this) //TODO Material Design?
        val dialogLayout = LayoutInflater.from(this).inflate(R.layout.add_isbn_dialog, null)
        dialog.setView(dialogLayout)

        dialog.setTitle("ISBN of New Book")
        dialog.setPositiveButton("OK") { d, i ->
            presenter.addBook(dialogLayout.findViewById<EditText>(R.id.isdn_edittext).text.toString())
        }
        dialog.show()
    }
}
