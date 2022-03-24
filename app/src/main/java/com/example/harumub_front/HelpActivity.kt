package com.example.harumub_front

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView

class HelpActivity: AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    lateinit var help_this : androidx.drawerlayout.widget.DrawerLayout
    lateinit var drawer_button : ImageButton
    lateinit var recent_button: ImageButton
    lateinit var drawer_view : NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        help_this = findViewById(R.id.help_drawer)
        drawer_button = findViewById(R.id.drawer_button) // 드로어 열기(메뉴버튼)
        drawer_view = findViewById(R.id.drawer_view) // 드로어
        val drawerHeader = drawer_view.getHeaderView(0) // 드로어 헤더
        recent_button = findViewById(R.id.recent) // 최근 감상기록 버튼


        // 드로어 버튼 클릭 -> 드로어 메뉴 열기
        drawer_button.setOnClickListener{
            help_this.openDrawer(GravityCompat.START) // START = left, END : right (드로어가 나오는 방향지정)
        }
        // 네비게이션 메뉴 아이템에 클릭 속성 부여
        drawer_view.setNavigationItemSelectedListener(this)

        // 최근 감상 기록 (시계) 버튼 클릭 -> 드로어 메뉴 열기
        recent_button.setOnClickListener{
            val intent = Intent(this, MyMovieListActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { // 네비게이션 메뉴 아이템 클릭 시 수행
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
        help_this.closeDrawers() // 네비게이션 뷰 닫기
        return false
    }
}