package com.example.whatsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var countryCode:String
    private lateinit var phoneNumber:String
    var mCredentialsApiClient:GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Add Hint Request Feature



        editText.addTextChangedListener {
            if (it != null) {
                if(it.length>=9){
                    materialButton.isEnabled = true
                } else{
                    materialButton.isEnabled = false
                }
            }
        }

        materialButton.setOnClickListener {
            countryCode = countryCodePicker.selectedCountryCodeWithPlus
            phoneNumber = countryCode + editText.text.toString()
            closeKeyboard()
            notifyUser()
        }

    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    private fun notifyUser() {
        MaterialAlertDialogBuilder(this).apply {
            setMessage(
                "We will be verifying the phone number:$phoneNumber\n" + "Is this ok , or would you like to edit this " +
                        "number?"
            )
            setPositiveButton("Ok"){ _, _->
                showOtpActivity()
            }
            setNegativeButton("Edit"){ dialog, which->
                  dialog.dismiss()
            }
            setCancelable(false)
            create()
            show()
        }
    }

    private fun showOtpActivity() {

        startActivity(Intent(this, OtpActivity::class.java).putExtra(PHONE_NUMBER, phoneNumber))
        finish()

    }


}