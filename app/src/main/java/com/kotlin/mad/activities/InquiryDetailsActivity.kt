package com.kotlin.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kotlin.mad.R
import com.kotlin.mad.models.InquiryModel
import com.google.firebase.database.FirebaseDatabase

class InquiryDetailsActivity : AppCompatActivity() {

    private lateinit var tvCId: TextView
    private lateinit var tvCName: TextView
    private lateinit var tvCNumber: TextView
    private lateinit var tvCType: TextView
    private lateinit var tvCInquiry: TextView

    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inquiry_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("cId").toString(),
                intent.getStringExtra("cName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("cId").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("InquiryDB").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, " data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, InquiryFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }



//chack

    private fun initView() {
        tvCId = findViewById(R.id.tvCId)
        tvCName = findViewById(R.id.tvCName)
        tvCNumber = findViewById(R.id.tvCNumber)
        tvCType = findViewById(R.id.tvCType)
        tvCInquiry = findViewById(R.id.tvCInquiry)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //passing data
        tvCId.text = intent.getStringExtra("cId")
        tvCName.text = intent.getStringExtra("cName")
        tvCNumber.text = intent.getStringExtra("cNumber")
        tvCType.text = intent.getStringExtra("cType")
        tvCInquiry.text = intent.getStringExtra("cInquiry")

    }

    private fun openUpdateDialog(
        cId: String,
        cName: String

    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etCName = mDialogView.findViewById<EditText>(R.id.etCName)
        val etCNumber = mDialogView.findViewById<EditText>(R.id.etCNumber)
        val etCType = mDialogView.findViewById<EditText>(R.id.etCType)
        val etCInquiry = mDialogView.findViewById<EditText>(R.id.etCInquiry)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        //update
        etCName.setText(intent.getStringExtra("cName").toString())
        etCNumber.setText(intent.getStringExtra("cNumber").toString())
        etCType.setText(intent.getStringExtra("cType").toString())
        etCInquiry.setText(intent.getStringExtra("cInquiry").toString())

        mDialog.setTitle("Updating $cName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateInquiryData(
                cId,
                etCName.text.toString(),
                etCNumber.text.toString(),
                etCType.text.toString(),
                etCInquiry.text.toString()

            )

            Toast.makeText(applicationContext, " Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our text views
            tvCName.text = etCName.text.toString()
            tvCNumber.text = etCNumber.text.toString()
            tvCType.text = etCType.text.toString()
            tvCInquiry.text = etCInquiry.text.toString()

            alertDialog.dismiss()

        }

    }

    private fun updateInquiryData(
        id: String,
        name: String,
        number: String,
        type: String,
        inquiry: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("InquiryDB").child(id)
        val inquiryInfo = InquiryModel(id, name, number, type, inquiry)
        dbRef.setValue(inquiryInfo)
    }
}