package com.kotlin.mad.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.kotlin.mad.models.DeliveryModel
import com.kotlin.mad.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DeliveryInsertionActivity : AppCompatActivity() {

    //initializing variables

    private lateinit var etCName: EditText
    private lateinit var etCNumber: EditText
    private lateinit var etCAddress: EditText
    private lateinit var etCEmail: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etCName = findViewById(R.id.etCName)
        etCNumber = findViewById(R.id.etCNumber)
        etCAddress = findViewById(R.id.etCAddress)
        etCEmail = findViewById(R.id.etCEmail)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("DeliveryDB")

        btnSaveData.setOnClickListener {
            saveDeliveryData()
        }

    }

    private fun saveDeliveryData() {

        //Geting Values
        val cName = etCName.text.toString()
        val cNumber = etCNumber.text.toString()
        val cAddress = etCAddress.text.toString()
        val cEmail = etCEmail.text.toString()

        //validation
        if (cName.isEmpty() || cNumber.isEmpty() || cAddress.isEmpty() || cEmail.isEmpty()) {

            if (cName.isEmpty()) {
                etCName.error = "Please Enter Customer Name"
            }
            if (cNumber.isEmpty()) {
                etCNumber.error = "Please Enter Customer Number"
            }
            if (cAddress.isEmpty()) {
                etCAddress.error = "Please Enter Address"
            }
            if (cEmail.isEmpty()) {
                etCEmail.error = "Please Enter Email"
            }
            Toast.makeText(this, "please check Some areas are not filled", Toast.LENGTH_LONG).show()
        } else {

            //genrate unique ID
            val cId = dbRef.push().key!!

            val bill = DeliveryModel(cId, cName, cNumber, cAddress, cEmail)

            dbRef.child(cId).setValue(bill)
                .addOnCompleteListener {
                    Toast.makeText(this, "All data insert successfully", Toast.LENGTH_SHORT).show()

                    //clear data after insert
                    etCName.text.clear()
                    etCNumber.text.clear()
                    etCAddress.text.clear()
                    etCEmail.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}