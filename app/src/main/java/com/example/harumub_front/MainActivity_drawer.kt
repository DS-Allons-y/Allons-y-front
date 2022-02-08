package com.example.harumub_front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import org.w3c.dom.Text

class MainActivity_drawer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var main_this : androidx.drawerlayout.widget.DrawerLayout
    lateinit var drawer_button : ImageButton
    lateinit var drawer_view : NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drawer)
        main_this = findViewById(R.id.main_drawer)

        drawer_button = findViewById(R.id.drawer_button) // 드로어 열기(메뉴버튼)
        drawer_view = findViewById(R.id.drawer_view) // 드로어
        val drawerHeader = drawer_view.getHeaderView(0) // 드로어 헤더

        drawer_button.setOnClickListener{
            main_this.openDrawer(GravityCompat.START) // START = left, END : right (드로어가 나오는 방향지정)
        }
        drawer_view.setNavigationItemSelectedListener(this) // 네비게이션 메뉴 아이템에 클릭 속성 부여

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean { // 네비게이션 메뉴 아이템 클릭 시 수행
        when(item.itemId){ // 드로어 메뉴 눌렸을 시 수행. 수정 필요
            R.id.drawer_UserRecord -> Toast.makeText(applicationContext, "사용자 기록보기", Toast.LENGTH_SHORT).show()
            R.id.drawer_WatchAlone -> Toast.makeText(applicationContext, "혼자 보기", Toast.LENGTH_SHORT).show()
            R.id.drawer_WatchTogether -> Toast.makeText(applicationContext, "같이 보기", Toast.LENGTH_SHORT).show()
            R.id.drawer_Help -> Toast.makeText(applicationContext, "도움!", Toast.LENGTH_SHORT).show()
            R.id.drawer_Logout -> Toast.makeText(applicationContext, "로그아웃", Toast.LENGTH_SHORT).show()
        }
        main_this.closeDrawers() // 네비게이션 뷰 닫기
        return false
    }
}
