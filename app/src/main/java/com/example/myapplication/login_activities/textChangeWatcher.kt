package com.example.myapplication.login_activities

import android.text.Editable
import android.text.TextWatcher
import com.example.myapplication.interfaces.IAuthView

class TextChangeWatcher(private val view: IAuthView):TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        view.turnOffAllErrors()
        view.showPasswordToggle()
    }
}