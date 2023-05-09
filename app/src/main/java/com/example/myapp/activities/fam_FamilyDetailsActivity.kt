package com.example.myapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.models.fam_FamilyModel
import com.example.myapp.R
import com.google.firebase.database.FirebaseDatabase

class fam_FamilyDetailsActivity : AppCompatActivity() {

    private lateinit var tvFamId: TextView
    private lateinit var tvFamName: TextView
    private lateinit var tvFamAdd: TextView
    private lateinit var tvFamDes: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fam_activity_family_details)

        val backNavImageView = findViewById<ImageView>(R.id.iv_backnav)
        backNavImageView.setOnClickListener {
            onBackPressed()
         }

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("famId").toString(),
                intent.getStringExtra("famName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("famId").toString()
            )
        }

    }

    private fun initView() {
        tvFamId = findViewById(R.id.tvFamId)
        tvFamName = findViewById(R.id.tvFamName)
        tvFamAdd = findViewById(R.id.tvFamAdd)
        tvFamDes = findViewById(R.id.tvFamDes)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvFamId.text = intent.getStringExtra("famId")
        tvFamName.text = intent.getStringExtra("famName")
        tvFamAdd.text = intent.getStringExtra("famAdd")
        tvFamDes.text = intent.getStringExtra("famDes")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Families").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Family Details Deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, fam_FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun openUpdateDialog(
        famId: String,
        famName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.fam_update_dialog, null)

        mDialog.setView(mDialogView)

        val etFamName = mDialogView.findViewById<EditText>(R.id.etFamName)
        val etFamAdd = mDialogView.findViewById<EditText>(R.id.etFamAdd)
        val etFamDes = mDialogView.findViewById<EditText>(R.id.etFamDes)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etFamName.setText(intent.getStringExtra("famName").toString())
        etFamAdd.setText(intent.getStringExtra("famAdd").toString())
        etFamDes.setText(intent.getStringExtra("famDes").toString())

        mDialog.setTitle("Updating $famName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateFamData(
                famId,
                etFamName.text.toString(),
                etFamAdd.text.toString(),
                etFamDes.text.toString()
            )

            Toast.makeText(applicationContext, "Family Details Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvFamName.text = etFamName.text.toString()
            tvFamAdd.text = etFamAdd.text.toString()
            tvFamDes.text = etFamDes.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateFamData(
        id: String,
        name: String,
        address: String,
        description: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Families").child(id)
        val famInfo = fam_FamilyModel(id, name, address, description)
        dbRef.setValue(famInfo)
    }

}