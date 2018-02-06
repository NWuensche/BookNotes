package com.nwuensche.booknotes.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import com.nwuensche.booknotes.R
import com.nwuensche.booknotes.model.Book
import com.nwuensche.booknotes.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputEditText
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever
import android.R.attr.data
import android.app.Activity
import android.support.v7.app.AlertDialog
import android.view.SubMenu




class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, com.nwuensche.booknotes.view.MenuView {


    private lateinit var presenter: MainPresenter
    var list: ArrayList<MenuItem> = arrayListOf()
    override lateinit var context: Context

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
                toast("Saved!")
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
            "Add Book Photo" -> {
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
        alert {
            title = "ISBN of New Book"
            customView {
                val titleView = textInputEditText()
                okButton { presenter.addBook(titleView.text.toString()) }
            }
        }.show()
    }
}
