package com.example.myapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.adapters.fam_FamAdapter
import com.example.myapp.models.fam_FamilyModel
import com.example.myapp.R
import com.google.firebase.database.*

class fam_FetchingActivity : AppCompatActivity() {

    private lateinit var famRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var famList: ArrayList<fam_FamilyModel>
    private lateinit var dbRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fam_activity_fetching)

        val backNavImageView = findViewById<ImageView>(R.id.iv_backnav)
        backNavImageView.setOnClickListener {
            onBackPressed()
        }

        famRecyclerView = findViewById(R.id.rvFam)
        famRecyclerView.layoutManager = LinearLayoutManager(this)
        famRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        famList = arrayListOf<fam_FamilyModel>()

        getFamilyData()

    }

    private fun getFamilyData() {

        famRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Families")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                famList.clear()
                if (snapshot.exists()){
                    for (famSnap in snapshot.children){
                        val famData = famSnap.getValue(fam_FamilyModel::class.java)
                        famList.add(famData!!)
                    }
                    val mAdapter = fam_FamAdapter(famList)
                    famRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : fam_FamAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@fam_FetchingActivity, fam_FamilyDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("famId", famList[position].famId)
                            intent.putExtra("famName", famList[position].famName)
                            intent.putExtra("famAdd", famList[position].famAdd)
                            intent.putExtra("famDes", famList[position].famDes)
                            startActivity(intent)
                        }

                    })

                    famRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}