package com.example.harumub_front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_my_movie_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class WatchListActivity : AppCompatActivity()
//    , NavigationView.OnNavigationItemSelectedListener
{

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<WatchListAdapter.ViewHolder>? = null

//    lateinit var main_this : androidx.drawerlayout.widget.DrawerLayout
//    lateinit var drawer_button : ImageButton
//    lateinit var recent_button: ImageButton
//    lateinit var drawer_view : NavigationView

    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInterface

    // 현재 로그인하고 있는 사용자 아이디
    lateinit var id : String

    // 추천 정보
    lateinit var reco1_titleArray : ArrayList<String>
    lateinit var reco1_posterArray : ArrayList<String>

    lateinit var reco2_1_userId : String
    lateinit var reco2_2_userId : String
    lateinit var reco2_3_userId : String
    lateinit var reco2_4_userId : String
    lateinit var reco2_5_userId : String

    lateinit var reco2_1_title : ArrayList<String>
    lateinit var reco2_2_title : ArrayList<String>
    lateinit var reco2_3_title : ArrayList<String>
    lateinit var reco2_4_title : ArrayList<String>
    lateinit var reco2_5_title : ArrayList<String>

    lateinit var reco2_1_poster : ArrayList<String>
    lateinit var reco2_2_poster : ArrayList<String>
    lateinit var reco2_3_poster : ArrayList<String>
    lateinit var reco2_4_poster : ArrayList<String>
    lateinit var reco2_5_poster : ArrayList<String>

    lateinit var reco3_titleArray : ArrayList<String>
    lateinit var reco3_posterArray : ArrayList<String>

    lateinit var reco4_year : String
    lateinit var reco4_titleArray : ArrayList<String>
    lateinit var reco4_posterArray : ArrayList<String>

    lateinit var reco6_titleArray : ArrayList<String>
    lateinit var reco6_posterArray : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_movie_list)

        id = intent.getStringExtra("user_id").toString()

        reco1_titleArray = intent.getSerializableExtra("reco1_titleArray") as ArrayList<String>
        reco1_posterArray = intent.getSerializableExtra("reco1_posterArray") as ArrayList<String>

        reco2_1_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_2_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_3_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_4_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_5_userId = intent.getStringExtra("reco2_1_userId").toString()

        reco2_1_title = intent.getSerializableExtra("reco2_1_title") as ArrayList<String>
        reco2_2_title = intent.getSerializableExtra("reco2_2_title") as ArrayList<String>
        reco2_3_title = intent.getSerializableExtra("reco2_3_title") as ArrayList<String>
        reco2_4_title = intent.getSerializableExtra("reco2_4_title") as ArrayList<String>
        reco2_5_title = intent.getSerializableExtra("reco2_5_title") as ArrayList<String>

        reco2_1_poster = intent.getSerializableExtra("reco2_1_poster") as ArrayList<String>
        reco2_2_poster = intent.getSerializableExtra("reco2_2_poster") as ArrayList<String>
        reco2_3_poster = intent.getSerializableExtra("reco2_3_poster") as ArrayList<String>
        reco2_4_poster = intent.getSerializableExtra("reco2_4_poster") as ArrayList<String>
        reco2_5_poster = intent.getSerializableExtra("reco2_5_poster") as ArrayList<String>

        reco3_titleArray = intent.getSerializableExtra("reco3_titleArray") as ArrayList<String>
        reco3_posterArray = intent.getSerializableExtra("reco3_posterArray") as ArrayList<String>

        reco4_year = intent.getStringExtra("reco4_year").toString()
        reco4_titleArray = intent.getSerializableExtra("reco4_titleArray") as java.util.ArrayList<String>
        reco4_posterArray = intent.getSerializableExtra("reco4_posterArray") as java.util.ArrayList<String>

        reco6_titleArray = intent.getSerializableExtra("reco6_titleArray") as java.util.ArrayList<String>
        reco6_posterArray = intent.getSerializableExtra("reco6_posterArray") as java.util.ArrayList<String>

        // 메인 페이지에서 전달받은 인텐트 데이터 확인
        if (intent.hasExtra("user_id")) {
            Log.e("WatchListActivity", "메인에서 받아온 id : $id")
        } else {
            Log.e("WatchListActivity", "가져온 데이터 없음")
        }

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        var result : WatchListResult
        var titleArray : ArrayList<String>
        var posterArray : ArrayList<String>

        var map = HashMap<String, String>()
        map.put("id", id!!)

        var call = retrofitInterface.executeWatchList(map)

        call!!.enqueue(object : Callback<WatchListResult?>{
            override fun onResponse(call: Call<WatchListResult?>, response: Response<WatchListResult?>) {
                if(response.code() == 200){
                    result = response.body()!!

                    titleArray = result.title
                    posterArray = result.poster

                    // 나의 감상기록 RecyclerView와 WatchListAdapter 연결
                    layoutManager = GridLayoutManager(this@WatchListActivity, 3, GridLayoutManager.VERTICAL, false)
                    recyclerView.layoutManager = layoutManager
                    adapter = WatchListAdapter(id, titleArray, posterArray, reco1_titleArray, reco1_posterArray,
                        reco2_1_userId, reco2_2_userId, reco2_3_userId, reco2_4_userId, reco2_5_userId,
                        reco2_1_title, reco2_2_title, reco2_3_title, reco2_4_title, reco2_5_title,
                        reco2_1_poster, reco2_2_poster, reco2_3_poster, reco2_4_poster, reco2_5_poster,
                        reco3_titleArray, reco3_posterArray, reco4_year, reco4_titleArray, reco4_posterArray,
                        reco6_titleArray, reco6_posterArray)
                    recyclerView.adapter = adapter

                    //Toast.makeText(this@WatchListActivity, "get movie list successfully", Toast.LENGTH_SHORT).show()
                }
                else if (response.code() == 400){
                    //Toast.makeText(this@WatchListActivity, "get movie list error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<WatchListResult?>, t: Throwable) {
                //Toast.makeText(this@WatchListActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })

  /*
        main_this = findViewById(R.id.main_drawer)
        drawer_button = findViewById(R.id.drawer_button) // 드로어 열기(메뉴버튼)
        drawer_view = findViewById(R.id.drawer_view) // 드로어
        val drawerHeader = drawer_view.getHeaderView(0) // 드로어 헤더
        recent_button = findViewById(R.id.recent) // 최근 감상기록 버튼

        // 드로어 버튼 클릭 -> 드로어 메뉴 열기
        drawer_button.setOnClickListener{
            main_this.openDrawer(GravityCompat.START) // START = left, END : right (드로어가 나오는 방향지정)
        }
        // 네비게이션 메뉴 아이템에 클릭 속성 부여
        drawer_view.setNavigationItemSelectedListener(this)

        // 최근 감상 기록 (시계) 버튼 클릭 -> 페이지 이동
        recent_button.setOnClickListener{
            val intent = Intent(this, WatchListActivity::class.java)
            intent.putExtra("user_id", id)

            intent.putExtra("reco1_titleArray", reco1_titleArray)
            intent.putExtra("reco1_posterArray", reco1_posterArray)

            intent.putExtra("reco2_1_userId", reco2_1_userId)
            intent.putExtra("reco2_2_userId", reco2_2_userId)
            intent.putExtra("reco2_3_userId", reco2_3_userId)
            intent.putExtra("reco2_4_userId", reco2_4_userId)
            intent.putExtra("reco2_5_userId", reco2_5_userId)

            intent.putExtra("reco2_1_title", reco2_1_title)
            intent.putExtra("reco2_2_title", reco2_2_title)
            intent.putExtra("reco2_3_title", reco2_3_title)
            intent.putExtra("reco2_4_title", reco2_4_title)
            intent.putExtra("reco2_5_title", reco2_5_title)

            intent.putExtra("reco2_1_poster", reco2_1_poster)
            intent.putExtra("reco2_2_poster", reco2_2_poster)
            intent.putExtra("reco2_3_poster", reco2_3_poster)
            intent.putExtra("reco2_4_poster", reco2_4_poster)
            intent.putExtra("reco2_5_poster", reco2_5_poster)

            intent.putExtra("reco3_titleArray", reco3_titleArray)
            intent.putExtra("reco3_posterArray", reco3_posterArray)

            intent.putExtra("reco4_year", reco4_year)
            intent.putExtra("reco4_titleArray", reco4_titleArray)
            intent.putExtra("reco4_posterArray", reco4_posterArray)

            intent.putExtra("reco6_titleArray", reco6_titleArray)
            intent.putExtra("reco6_posterArray", reco6_posterArray)

            startActivity(intent)
        }
*/
        // 메인으로 돌아가는 버튼
        back2main.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user_id", id)

            intent.putExtra("reco1_titleArray", reco1_titleArray)
            intent.putExtra("reco1_posterArray", reco1_posterArray)

            intent.putExtra("reco2_1_userId", reco2_1_userId)
            intent.putExtra("reco2_2_userId", reco2_2_userId)
            intent.putExtra("reco2_3_userId", reco2_3_userId)
            intent.putExtra("reco2_4_userId", reco2_4_userId)
            intent.putExtra("reco2_5_userId", reco2_5_userId)

            intent.putExtra("reco2_1_title", reco2_1_title)
            intent.putExtra("reco2_2_title", reco2_2_title)
            intent.putExtra("reco2_3_title", reco2_3_title)
            intent.putExtra("reco2_4_title", reco2_4_title)
            intent.putExtra("reco2_5_title", reco2_5_title)

            intent.putExtra("reco2_1_poster", reco2_1_poster)
            intent.putExtra("reco2_2_poster", reco2_2_poster)
            intent.putExtra("reco2_3_poster", reco2_3_poster)
            intent.putExtra("reco2_4_poster", reco2_4_poster)
            intent.putExtra("reco2_5_poster", reco2_5_poster)

            intent.putExtra("reco3_titleArray", reco3_titleArray)
            intent.putExtra("reco3_posterArray", reco3_posterArray)

            intent.putExtra("reco4_year", reco4_year)
            intent.putExtra("reco4_titleArray", reco4_titleArray)
            intent.putExtra("reco4_posterArray", reco4_posterArray)

            intent.putExtra("reco6_titleArray", reco6_titleArray)
            intent.putExtra("reco6_posterArray", reco6_posterArray)

            startActivity(intent)
        }

    }

  /*
    override fun onNavigationItemSelected(item: MenuItem): Boolean {// 네비게이션 메뉴 아이템 클릭 시 수행
        when(item.itemId){ // 드로어 메뉴 눌렸을 시 수행. 수정 필요
            R.id.drawer_UserRecord -> {
                // fragment manager 가져와서 fragment transaction 생성
                with(supportFragmentManager.beginTransaction()) {
                    //Toast.makeText(applicationContext, "사용자 기록보기", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, WatchListActivity::class.java)
                    intent.putExtra("user_id", id)

                    intent.putExtra("reco1_titleArray", reco1_titleArray)
                    intent.putExtra("reco1_posterArray", reco1_posterArray)

                    intent.putExtra("reco2_1_userId", reco2_1_userId)
                    intent.putExtra("reco2_2_userId", reco2_2_userId)
                    intent.putExtra("reco2_3_userId", reco2_3_userId)
                    intent.putExtra("reco2_4_userId", reco2_4_userId)
                    intent.putExtra("reco2_5_userId", reco2_5_userId)

                    intent.putExtra("reco2_1_title", reco2_1_title)
                    intent.putExtra("reco2_2_title", reco2_2_title)
                    intent.putExtra("reco2_3_title", reco2_3_title)
                    intent.putExtra("reco2_4_title", reco2_4_title)
                    intent.putExtra("reco2_5_title", reco2_5_title)

                    intent.putExtra("reco2_1_poster", reco2_1_poster)
                    intent.putExtra("reco2_2_poster", reco2_2_poster)
                    intent.putExtra("reco2_3_poster", reco2_3_poster)
                    intent.putExtra("reco2_4_poster", reco2_4_poster)
                    intent.putExtra("reco2_5_poster", reco2_5_poster)

                    intent.putExtra("reco3_titleArray", reco3_titleArray)
                    intent.putExtra("reco3_posterArray", reco3_posterArray)

                    intent.putExtra("reco4_year", reco4_year)
                    intent.putExtra("reco4_titleArray", reco4_titleArray)
                    intent.putExtra("reco4_posterArray", reco4_posterArray)

                    intent.putExtra("reco6_titleArray", reco6_titleArray)
                    intent.putExtra("reco6_posterArray", reco6_posterArray)

                    startActivityForResult(intent, 0) // + 결과값 전달 // requestCode: 액티비티 식별값 - 원하는 값
                    commit()
                }
            }
            R.id.drawer_WatchAlone -> {
                with(supportFragmentManager.beginTransaction()) {
                    //Toast.makeText(applicationContext, "혼자 보기", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, SearchActivity::class.java)
                    intent.putExtra("user_id", id)

                    intent.putExtra("reco1_titleArray", reco1_titleArray)
                    intent.putExtra("reco1_posterArray", reco1_posterArray)

                    intent.putExtra("reco2_1_userId", reco2_1_userId)
                    intent.putExtra("reco2_2_userId", reco2_2_userId)
                    intent.putExtra("reco2_3_userId", reco2_3_userId)
                    intent.putExtra("reco2_4_userId", reco2_4_userId)
                    intent.putExtra("reco2_5_userId", reco2_5_userId)

                    intent.putExtra("reco2_1_title", reco2_1_title)
                    intent.putExtra("reco2_2_title", reco2_2_title)
                    intent.putExtra("reco2_3_title", reco2_3_title)
                    intent.putExtra("reco2_4_title", reco2_4_title)
                    intent.putExtra("reco2_5_title", reco2_5_title)

                    intent.putExtra("reco2_1_poster", reco2_1_poster)
                    intent.putExtra("reco2_2_poster", reco2_2_poster)
                    intent.putExtra("reco2_3_poster", reco2_3_poster)
                    intent.putExtra("reco2_4_poster", reco2_4_poster)
                    intent.putExtra("reco2_5_poster", reco2_5_poster)

                    intent.putExtra("reco3_titleArray", reco3_titleArray)
                    intent.putExtra("reco3_posterArray", reco3_posterArray)

                    intent.putExtra("reco4_year", reco4_year)
                    intent.putExtra("reco4_titleArray", reco4_titleArray)
                    intent.putExtra("reco4_posterArray", reco4_posterArray)

                    intent.putExtra("reco6_titleArray", reco6_titleArray)
                    intent.putExtra("reco6_posterArray", reco6_posterArray)

                    startActivityForResult(intent, 0) // + 결과값 전달 // requestCode: 액티비티 식별값 - 원하는 값
                    commit()
                }
            }
            R.id.drawer_WatchTogether -> {
                with(supportFragmentManager.beginTransaction()) {
                    //Toast.makeText(applicationContext, "같이 보기", Toast.LENGTH_SHORT).show()

                    val intent = Intent(applicationContext, EnterActivity::class.java)
                    intent.putExtra("user_id", id)
                    intent.putExtra("reco1_titleArray", reco1_titleArray)
                    intent.putExtra("reco1_posterArray", reco1_posterArray)
                    intent.putExtra("reco2_1_userId", reco2_1_userId)
                    intent.putExtra("reco2_2_userId", reco2_2_userId)
                    intent.putExtra("reco2_3_userId", reco2_3_userId)
                    intent.putExtra("reco2_4_userId", reco2_4_userId)
                    intent.putExtra("reco2_5_userId", reco2_5_userId)
                    intent.putExtra("reco2_1_title", reco2_1_title)
                    intent.putExtra("reco2_2_title", reco2_2_title)
                    intent.putExtra("reco2_3_title", reco2_3_title)
                    intent.putExtra("reco2_4_title", reco2_4_title)
                    intent.putExtra("reco2_5_title", reco2_5_title)
                    intent.putExtra("reco2_1_poster", reco2_1_poster)
                    intent.putExtra("reco2_2_poster", reco2_2_poster)
                    intent.putExtra("reco2_3_poster", reco2_3_poster)
                    intent.putExtra("reco2_4_poster", reco2_4_poster)
                    intent.putExtra("reco2_5_poster", reco2_5_poster)
                    intent.putExtra("reco3_titleArray", reco3_titleArray)
                    intent.putExtra("reco3_posterArray", reco3_posterArray)

                    intent.putExtra("reco4_year", reco4_year)
                    intent.putExtra("reco4_titleArray", reco4_titleArray)
                    intent.putExtra("reco4_posterArray", reco4_posterArray)

                    intent.putExtra("reco6_titleArray", reco6_titleArray)
                    intent.putExtra("reco6_posterArray", reco6_posterArray)

                    startActivityForResult(intent, 0) // + 결과값 전달 // requestCode: 액티비티 식별값 - 원하는 값
                    commit()
                }
            }
            R.id.drawer_Help -> {
                with(supportFragmentManager.beginTransaction()) {
                    //Toast.makeText(applicationContext, "도움말", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, HelpActivity::class.java)
                    startActivityForResult(intent, 0)
                    commit()
                } // 프래그먼트 트랜잭션 변경 후 commit() 호출해야 변경 내용 적용
            }
            R.id.drawer_Logout -> {
                with(supportFragmentManager.beginTransaction()) {
                    val map = HashMap<String, String>()

                    val call = retrofitInterface.executeLogout(map)
                    call!!.enqueue(object : Callback<Void?> {
                        override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                            if (response.code() == 200) {
                                var intent = Intent(applicationContext, LoginActivity::class.java) // 두번째 인자에 이동할 액티비티
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

                                Toast.makeText(this@WatchListActivity, "로그아웃합니다..", Toast.LENGTH_LONG).show()
                                startActivity(intent)
                            }
                        }

                        override fun onFailure(call: Call<Void?>, t: Throwable) {
                            //Toast.makeText(this@WatchListActivity, t.message, Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
        }
        main_this.closeDrawers() // 네비게이션 뷰 닫기
        return false
    }
  */
}