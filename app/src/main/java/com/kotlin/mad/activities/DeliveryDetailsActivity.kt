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
import com.kotlin.mad.models.DeliveryModel
import com.google.firebase.database.FirebaseDatabase

class DeliveryDetailsActivity : AppCompatActivity() {

    private lateinit var tvCId: TextView
    private lateinit var tvCName: TextView
    private lateinit var tvCNumber: TextView
    private lateinit var tvCAddress: TextView
    private lateinit var tvCEmail: TextView

    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_details)

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
        val dbRef = FirebaseDatabase.getInstance().getReference("DeliveryDB").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, " data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, DeliveryFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }





    private fun initView() {
        tvCId = findViewById(R.id.tvCId)
        tvCName = findViewById(R.id.tvCName)
        tvCNumber = findViewById(R.id.tvCNumber)
        tvCAddress = findViewById(R.id.tvCAddress)
        tvCEmail = findViewById(R.id.tvCEmail)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //passing data
        tvCId.text = intent.getStringExtra("cId")
        tvCName.text = intent.getStringExtra("cName")
        tvCNumber.text = intent.getStringExtra("cNumber")
        tvCAddress.text = intent.getStringExtra("cAddress")
        tvCEmail.text = intent.getStringExtra("cEmail")

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
        val etCAddress = mDialogView.findViewById<EditText>(R.id.etCAddress)
        val etCEmail = mDialogView.findViewById<EditText>(R.id.etCEmail)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        //update
        etCName.setText(intent.getStringExtra("cName").toString())
        etCNumber.setText(intent.getStringExtra("cNumber").toString())
        etCAddress.setText(intent.getStringExtra("cAddress").toString())
        etCEmail.setText(intent.getStringExtra("cEmail").toString())

        mDialog.setTitle("Updating $cName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateDeliveryData(
                cId,
                etCName.text.toString(),
                etCNumber.text.toString(),
                etCAddress.text.toString(),
                etCEmail.text.toString()

            )

            Toast.makeText(applicationContext, " Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our text views
            tvCName.text = etCName.text.toString()
            tvCNumber.text = etCNumber.text.toString()
            tvCAddress.text = etCAddress.text.toString()
            tvCEmail.text = etCEmail.text.toString()

            alertDialog.dismiss()

        }

    }

    private fun updateDeliveryData(
        id: String,
        name: String,
        number: String,
        address: String,
        email: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("DeliveryDB").child(id)
        val deliveryInfo = DeliveryModel(id, name, number, address, email)
        dbRef.setValue(deliveryInfo)
    }
}