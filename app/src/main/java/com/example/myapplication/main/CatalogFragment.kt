package com.example.myapplication.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.CatalogFragmentGridBinding
import com.example.myapplication.interfaces.ICatalog
import com.example.myapplication.interfaces.IToastRealmSessionErrors
import com.example.myapplication.models_and_DB.RealmItemData
import com.example.myapplication.models_and_DB.RealmUserData
import com.example.myapplication.objects.SharedPrefsIDs
import com.google.android.material.slider.RangeSlider
import com.jaredrummler.android.colorpicker.ColorPanelView
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.jaredrummler.android.colorpicker.ColorShape
import io.realm.kotlin.query.RealmResults

class CatalogFragment : Fragment(R.layout.catalog_fragment_grid), IToastRealmSessionErrors,
    ICatalog,  ItemRecyclerAdapter.IItemNodeListener {
    private var binding: CatalogFragmentGridBinding? = null
    private val presenter by lazy {
        PresenterCatalog(
            this, activity?.getSharedPreferences(
                SharedPrefsIDs.sharedPrefLogInData,
                Context.MODE_PRIVATE
            )
        )
    }
    private val recyclerAdapter = ItemRecyclerAdapter(this)
    private val filter = FilterValues(null, null, 80F, 20F , null, null)
    private var priceFilter: RangeSlider?=null
    private var menuColorFilter: ColorPanelView?=null
    private var inStockFilter:SwitchCompat?=null
    private var applyButton:Button?=null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = CatalogFragmentGridBinding.bind(view)
        initBinding()
        preparePriceFilter()
        setupListeners()
        super.onViewCreated(view, savedInstanceState)
        Log.d("debug", "onCreateCatalog completed")
    }

    override fun onDestroy() {
        presenter.onFragmentClose()
        super.onDestroy()
    }

    private fun initBinding(){
        priceFilter=binding?.navigationView
            ?.menu?.findItem(R.id.menuPriceFilter)?.actionView as RangeSlider?
        menuColorFilter=binding?.navigationView
            ?.menu?.findItem(R.id.menuColorFilter)?.actionView
            ?.findViewById(R.id.colorPick) as ColorPanelView?
        inStockFilter=binding?.navigationView
            ?.menu?.findItem(R.id.menuInStockFilter)
            ?.actionView?.findViewById(R.id.inStockFilter) as SwitchCompat?
        applyButton= binding?.navigationView
            ?.menu?.findItem(R.id.menuSubmitFilters)
            ?.actionView?.findViewById(R.id.apply_filters) as Button?
    }

    private fun setupListeners(){
        menuColorFilter?.setOnClickListener {
            createColorPickerDialog(1)
        }
        inStockFilter?.setOnCheckedChangeListener { _, isChecked ->
            filter.inStock = isChecked
            Log.d("debug", "inStock=" + filter.inStock)
        }
        //applying filters
        applyButton?.setOnClickListener {
            filter.minPrice = priceFilter?.values?.get(0)
            filter.maxPrice = priceFilter?.values?.get(1)
            filter.color = getColorFilterValue()
            filter.inStock = getInStockFilterValue()
            Log.d("debug", "Apply button")
            presenter.updateData(filter)
        }
        binding?.swipeRefresh?.setOnRefreshListener {
            presenter.updateData(filter)
            binding?.swipeRefresh?.isRefreshing = false
        }
        presenter.init()
        binding?.recView?.adapter = recyclerAdapter
        binding?.filterButton?.setOnClickListener {
            binding?.drawerLayout?.openDrawer(GravityCompat.END)
        }
        binding?.navigationView?.getHeaderView(0)
            ?.findViewById<ImageButton>(R.id.exitAccountButton)
            ?.setOnClickListener {
                presenter.logOut()
            }
    }

    private fun getColorFilterValue(): Int? {
        if ((binding?.navigationView
                ?.menu?.findItem(R.id.menuColorFilter)
                ?.actionView
                ?.findViewById(R.id.checkBoxColorFilter) as CheckBox).isChecked
        )
            return menuColorFilter?.color
        return null
    }

    private fun getInStockFilterValue(): Boolean? {
        if ((binding?.navigationView
                ?.menu?.findItem(R.id.menuInStockFilter)
                ?.actionView?.findViewById(R.id.checkBoxInStock) as CheckBox).isChecked
        )
            return inStockFilter?.isChecked
        return null
    }

    override fun setPriceFilterValueTo(value: Float) {
        priceFilter?.valueTo = value
        priceFilter?.values?.set(1, value*0.8F)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun preparePriceFilter() {
        val menuPriceFilter = binding?.navigationView
            ?.menu?.findItem(R.id.menuPriceFilter)?.actionView as RangeSlider
        menuPriceFilter.addOnChangeListener { slider, _, _ ->
            Log.d("debug", "Slider THIS  values: " + slider.values[0] + " max " + slider.values[1])
            filter.minPrice = menuPriceFilter.values[0]
            filter.maxPrice = menuPriceFilter.values[1]
        }
    }

    private fun createColorPickerDialog(id: Int) {
        val dialog = ColorPickerDialog.newBuilder()
            .setColor(Color.RED)
            .setDialogType(ColorPickerDialog.TYPE_PRESETS)
            .setAllowCustom(true)
            .setAllowPresets(true)
            .setColorShape(ColorShape.CIRCLE)
            .setDialogId(id)
            .create()
        dialog.setColorPickerDialogListener(object : ColorPickerDialogListener {
            override fun onColorSelected(dialogId: Int, color: Int) {
                val colorIcon = binding?.navigationView
                    ?.menu?.findItem(R.id.menuColorFilter)
                    ?.actionView
                    ?.findViewById(R.id.colorPick) as ColorPanelView
                colorIcon.color = color
                filter.color = color
                Log.d("debug", "OnColorSelected inside fragment")
            }

            override fun onDialogDismissed(dialogId: Int) {

            }
        })
        dialog.show(childFragmentManager, "Color Picker")
    }

    override fun onItemClickListener(position: Int) {
        Log.d("debug", "item click: ${recyclerAdapter.getItems()?.get(position)?.itemName}")
        val bundle=Bundle()
        bundle.putString("itemId", recyclerAdapter.getItems()?.get(position)?._id)
        findNavController().navigate(R.id.action_catalogFragment_to_pdpFragment, bundle)
    // navigate(R.id.action_catalogFragment_to_pdpFragment, Bundle().putString("itemId","value"))
    }

    override fun setRecyclerData(itemList: RealmResults<RealmItemData>) {
        recyclerAdapter.updateList(itemList)
    }

    override fun setRecyclerLayoutManagerToGrid() {
        binding?.recView?.layoutManager = GridLayoutManager(context, 2)
    }

    override fun setupNavHeaderWithUserData(userData: RealmUserData) {
        binding?.navigationView
            ?.getHeaderView(0)?.findViewById<TextView>(R.id.tvUserFullName)
            ?.text="${userData.userFirstName} ${userData.userLastName}"
        binding?.navigationView
            ?.getHeaderView(0)?.findViewById<TextView>(R.id.tvUserLogin)
            ?.text=userData._id
    }

    override fun returnToLoginScreen() {
        findNavController().popBackStack(R.id.logInFragment, inclusive = false)
    }

    override fun showToastUnableToLogIN() {
        Toast.makeText(
            requireContext(),
            getString(R.string.logInErrorConnectionIssue),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun showToastInternalRealmError() {
        Toast.makeText(requireContext(), getString(R.string.unknownRealmError), Toast.LENGTH_LONG)
            .show()
    }

}