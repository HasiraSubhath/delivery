package com.kotlin.mad.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.kotlin.mad.models.InquiryModel
import com.kotlin.mad.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InquiryInsertionActivity : AppCompatActivity() {

    //initializing variables

    private lateinit var etCName: EditText
    private lateinit var etCNumber: EditText
    private lateinit var etCType: EditText
    private lateinit var etCInquiry: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etCName = findViewById(R.id.etCName)
        etCNumber = findViewById(R.id.etCNumber)
        etCType = findViewById(R.id.etCType)
        etCInquiry = findViewById(R.id.etCInquiry)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("InquiryDB")

        btnSaveData.setOnClickListener {
            saveInquiryData()
        }

    }

    private fun saveInquiryData() {

        //Geting Values
        val cName = etCName.text.toString()
        val cNumber = etCNumber.text.toString()
        val cType = etCType.text.toString()
        val cInquiry = etCInquiry.text.toString()

        //validation
        if (cName.isEmpty() || cNumber.isEmpty() || cType.isEmpty() || cInquiry.isEmpty()) {

            if (cName.isEmpty()) {
                etCName.error = "Please enter Customer Name"
            }
            if (cNumber.isEmpty()) {
                etCNumber.error = "Please Customer Number"
            }
            if (cType.isEmpty()) {
                etCType.error = "Please Inquiry Type"
            }
            if (cInquiry.isEmpty()) {
                etCInquiry.error = "Please Inquiry Description"
            }
            Toast.makeText(this, "please check Some areas are not filled", Toast.LENGTH_LONG).show()
        } else {

            //genrate unique ID
            val cId = dbRef.push().key!!

            val bill = InquiryModel(cId, cName, cNumber, cType, cInquiry)

            dbRef.child(cId).setValue(bill)
                .addOnCompleteListener {
                    Toast.makeText(this, "All data insert successfully", Toast.LENGTH_SHORT).show()

                    //clear data after insert
                    etCName.text.clear()
                    etCNumber.text.clear()
                    etCType.text.clear()
                    etCInquiry.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}