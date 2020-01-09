package com.micrositba.lifemonitor

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window

import androidx.fragment.app.DialogFragment

class InfoDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout to use as dialog
        val view = inflater.inflate(R.layout.dialog_info, container, false)
        view.findViewById<View>(R.id.DialogCloseButton).setOnClickListener {
            dismiss()  //Close Dialog
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction

        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
}

