package com.example.harumub_front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var main2_this : androidx.drawerlayout.widget.DrawerLayout
    lateinit var drawer_button : ImageButton
    lateinit var recent_button: ImageButton
    lateinit var drawer_view : NavigationView

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecommendAdapter1.ViewHolder>? = null

    private var layoutManager2: RecyclerView.LayoutManager? = null
    private var adapter2: RecyclerView.Adapter<RecommendAdapter2.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        main2_this = findViewById(R.id.main2_drawer)
        drawer_button = findViewById(R.id.drawer_button) // 드로어 열기(메뉴버튼)
        drawer_view = findViewById(R.id.drawer_view) // 드로어
        val drawerHeader = drawer_view.getHeaderView(0) // 드로어 헤더
        recent_button = findViewById(R.id.recent) // 최근 감상기록 버튼

        // 드로어 버튼 클릭 -> 드로어 메뉴 열기
        drawer_button.setOnClickListener{
            main2_this.openDrawer(GravityCompat.START) // START = left, END : right (드로어가 나오는 방향지정)
        }
        // 네비게이션 메뉴 아이템에 클릭 속성 부여
        drawer_view.setNavigationItemSelectedListener(this)

        // 최근 감상 기록 (시계) 버튼 클릭 -> 페이지 이동
        recent_button.setOnClickListener{
            val intent = Intent(this, MyMovieListActivity::class.java)
            startActivity(intent)
        }

        var text = findViewById<TextView>(R.id.textView3)

        layoutManager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        adapter = RecommendAdapter1()
        recyclerView.adapter = adapter

        layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.layoutManager = layoutManager2
        adapter2 = RecommendAdapter2()
        recyclerView2.adapter = adapter2

//        var recent = findViewById<ImageButton>(R.id.recent)
//        recent.setOnClickListener { // recent 이미지 버튼 클릭 시 나의 감상기록 페이지로 이동
//            val intent = Intent(this, MyMovieListActivity::class.java) // 메인2
//            startActivity(intent)
//        }

        // 원래는 이미지에서 넘어가야 함
        text.setOnClickListener { // textView3 클릭 시 다른 사용자 감상기록 페이지로 이동
            supportFragmentManager.beginTransaction()
                .replace(R.id.main2, UserMovieListFragment())
                .commit()
            Log.d("text : ", "선택")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {// 네비게이션 메뉴 아이템 클릭 시 수행
        when(item.itemId){ // 드로어 메뉴 눌렸을 시 수행. 수정 필요
            R.id.drawer_UserRecord -> {
                // fragment manager 가져와서 fragment transaction 생성
                with(supportFragmentManager.beginTransaction()) {
                    Toast.makeText(applicationContext, "사용자 기록보기", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, MyMovieListActivity::class.java)
                    startActivityForResult(intent, 0) // + 결과값 전달 // requestCode: 액티비티 식별값 - 원하는 값
                    commit()
                }
            }
            R.id.drawer_WatchAlone -> {
                with(supportFragmentManager.beginTransaction()) {
                    Toast.makeText(applicationContext, "혼자 보기", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, SearchActivity::class.java)
                    startActivityForResult(intent, 0) // + 결과값 전달 // requestCode: 액티비티 식별값 - 원하는 값
                    commit()
                }
            }
            R.id.drawer_WatchTogether -> {
                with(supportFragmentManager.beginTransaction()) {
                    Toast.makeText(applicationContext, "같이 보기", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, enterActivity::class.java)
                    startActivityForResult(intent, 0) // + 결과값 전달 // requestCode: 액티비티 식별값 - 원하는 값
                    commit()
                }
            }
            R.id.drawer_Help -> {
                with(supportFragmentManager.beginTransaction()) {
                    Toast.makeText(applicationContext, "도움말", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, HelpActivity::class.java)
                    startActivityForResult(intent, 0)
                    commit()
                } // 프래그먼트 트랜잭션 변경 후 commit() 호출해야 변경 내용 적용
            }
            R.id.drawer_Logout -> {
                with(supportFragmentManager.beginTransaction()) {
                    Toast.makeText(applicationContext, "로그아웃", Toast.LENGTH_SHORT).show()

                    // 로그아웃 기능 구현


                    var intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivityForResult(intent, 0)
                    commit()
                }
            }
        }
        main2_this.closeDrawers() // 네비게이션 뷰 닫기
        return false
    }
}