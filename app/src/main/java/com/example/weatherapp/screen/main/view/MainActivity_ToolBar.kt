package com.example.weatherapp.screen.main.view

import android.animation.LayoutTransition
import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.graphics.Color
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SearchView
import com.example.weatherapp.R

internal fun MainActivity.setUpToolbar() {
    setSupportActionBar(binding.toolBar)
}

internal fun MainActivity.handleToolBarMenu(menu: Menu) {
    menuInflater.inflate(R.menu.tool_bar_menu, menu)
    searchView = (menu.findItem(R.id.menu_search)?.actionView as SearchView)
    searchView.maxWidth = Integer.MAX_VALUE
    searchView.queryHint = getString(R.string.lbl_city_name)

    //Search view event
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(text: String?): Boolean {
            if (text == null) {
                searchView.onActionViewCollapsed()
                return false
            }
            onTextSubmit(text)
            return false
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            return false
        }
    })

    // Search view animation
    val searchViewId =
        searchView.context.resources.getIdentifier("android:id/search_bar", null, null)
    val searchBar: LinearLayout = searchView.findViewById(searchViewId) as LinearLayout
    searchBar.layoutTransition = LayoutTransition()

    //set hint text color
    val searchEdtId = resources.getIdentifier("android:id/search_src_text", null, null)
    val editText = searchView.findViewById<EditText>(searchEdtId)
    editText.setHintTextColor(Color.WHITE)

    val searchManager =
        getSystemService(SEARCH_SERVICE) as SearchManager
    searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
}

private fun MainActivity.onTextSubmit(text: String) {
    isAPISearch = true
    binding.includeProgress.frameProgress.visibility = View.VISIBLE
    mainViewModel.getLocationAPI(text)
    searchView.onActionViewCollapsed()
}