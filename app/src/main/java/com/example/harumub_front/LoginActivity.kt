package com.example.harumub_front

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.CubeGrid
import kotlinx.android.synthetic.main.dialog_spinkit.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class LoginActivity : AppCompatActivity() {
    private lateinit var progressDialog : ProgressDialog1    // 로딩 다이얼로그
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInterface

    lateinit var message : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        var l_id = findViewById<EditText>(R.id.myid)
        var l_pw = findViewById<EditText>(R.id.mypw)
        var btnLogin = findViewById<Button>(R.id.btn_login)
        var btnSignup = findViewById<Button>(R.id.btn_signup)

        // 로딩창 선언
        progressDialog = ProgressDialog1(this)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 백그라운드를 투명하게

        // 단방향 페이지 이동
        // 로그인 버튼 클릭 - 액티비티 종료 및 메인페이지 호출
        btnLogin.setOnClickListener {
            // 에뮬레이터 실행용
//            var intent = Intent(applicationContext, MainActivity2::class.java)
//            startActivity(intent)

            // 로딩창 실행
            // progressDialog.setCancelable(false) // 외부 클릭으로 다이얼로그 종료 X - 실행 위해 임시로 주석 처리
            progressDialog.show() // 로딩화면 보여주기

            // 사용자가 입력한 값들을 String으로 받아오기
            val id = l_id.text.toString()
            val pw = l_pw.text.toString()

            val map = HashMap<String, String>()
            map.put("id", id)
            map.put("password", pw)

            ///*
            val call = retrofitInterface.executeLogin(map)
            call!!.enqueue(object : Callback<LoginResult?> {
                override fun onResponse(call: Call<LoginResult?>, response: Response<LoginResult?>) {
                    if (response.code() == 200) {
                        val result = response.body()

                      /*
                        // 로그인 성공 다이얼로그
                        val builder1 = AlertDialog.Builder(this@LoginActivity)
                        builder1.setTitle("로그인 성공")
                        builder1.setMessage(result!!.name + "님 환영합니다!")
                        builder1.show()

                        val dig = android.app.AlertDialog.Builder(this@LoginActivity)
                        val dialogView =
                            View.inflate(this@LoginActivity, R.layout.dialog_login_success, null)
                        message = dialogView.findViewById(R.id.nameLogin)
                        message.text = result!!.name+"님 하루뭅에 오신 걸 환영합니다."
                        dig.setView(dialogView)
                        dig.show()
                      */

                        val reco1 = result!!.reco1 // 추천 1
                        val reco1_titleArray = reco1.titleArray // 추천 1의 추천 영화 제목 리스트
                        val reco1_posterArray = reco1.posterArray // 추천 1의 추천 영화 포스터 링크 리스트

                        val reco2_1 = result.reco2_1 // 유사 사용자 1
                        val reco2_2 = result.reco2_2 // 유사 사용자 2
                        val reco2_3 = result.reco2_3 // 유사 사용자 3
                        val reco2_4 = result.reco2_4 // 유사 사용자 4
                        val reco2_5 = result.reco2_5 // 유사 사용자 5

                        val reco2_1_userId = reco2_1.userId // 유사 사용자 1의 userId
                        val reco2_2_userId = reco2_2.userId // 유사 사용자 2의 userId
                        val reco2_3_userId = reco2_3.userId // 유사 사용자 3의 userId
                        val reco2_4_userId = reco2_4.userId // 유사 사용자 4의 userId
                        val reco2_5_userId = reco2_5.userId // 유사 사용자 5의 userId

                        val reco2_1_title = reco2_1.title // 유사 사용자 1의 추천 영화 제목 리스트
                        val reco2_2_title = reco2_2.title // 유사 사용자 2의 추천 영화 제목 리스트
                        val reco2_3_title = reco2_3.title // 유사 사용자 3의 추천 영화 제목 리스트
                        val reco2_4_title = reco2_4.title // 유사 사용자 4의 추천 영화 제목 리스트
                        val reco2_5_title = reco2_5.title // 유사 사용자 5의 추천 영화 제목 리스트

                        val reco2_1_poster = reco2_1.poster // 유사 사용자 1의 추천 영화 포스터 링크 리스트
                        val reco2_2_poster = reco2_2.poster // 유사 사용자 2의 추천 영화 포스터 링크 리스트
                        val reco2_3_poster = reco2_3.poster // 유사 사용자 3의 추천 영화 포스터 링크 리스트
                        val reco2_4_poster = reco2_4.poster // 유사 사용자 4의 추천 영화 포스터 링크 리스트
                        val reco2_5_poster = reco2_5.poster // 유사 사용자 5의 추천 영화 포스터 링크 리스트

                        val reco3 = result.reco3 // 선호 배우 영화 추천
                        val reco3_titleArray = reco3.titleArray // 추천 3의 추천 영화 제목 리스트
                        val reco3_posterArray = reco3.posterArray // 추천 3의 추천 영화 포스터 링크 리스트

                        val reco4 = result.reco4 // 연도별 영화 추천
                        val reco4_year = reco4.year // 추천 4의 추천 연도
                        val reco4_titleArray = reco4.titleArray // 추천 4의 추천 영화 제목 리스트
                        val reco4_posterArray = reco4.posterArray // 추천 4의 추천 영화 포스터 링크 리스트

                        // 리메이크 작품 추천(reco5) >> 결과 페이지

                        val reco6 = result.reco6 // 고전 TOP 10
                        val reco6_titleArray = reco6.titleArray // 추천 6의 추천 영화 제목 리스트
                        val reco6_posterArray = reco6.posterArray // 추천 6의 추천 영화 포스터 링크 리스트

                        // 메인2로 이동
                        var intent = Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("user_id", result.id)
                        intent.putExtra("user_name", result.name)

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

                        // 서버에서 성공한 신호(응답)를 받으면 로딩창 종료
                        progressDialog.dismiss()

                        Toast.makeText(this@LoginActivity, result.name + "님, 환영합니다.", Toast.LENGTH_SHORT).show()

                        startActivityForResult(intent, 0)
                    }
                    else if (response.code() == 400) {
                        //Toast.makeText(this@LoginActivity, "정의되지 않은 사용자", Toast.LENGTH_LONG).show()
                        Log.e("LoginActivity", "정의되지 않은 사용자: code 400")
                    }
                    else if (response.code() == 404) { // 로그인 실패 시 - 토스트 메시지, 애니메이션 종료
                        Toast.makeText(this@LoginActivity, "로그인 정보를 확인해주세요", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }
                override fun onFailure(call: Call<LoginResult?>, t: Throwable) {
                    //Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
                    Log.e("LoginActivity", t.message.toString())
                }
            })
            //*/

            /*  // 추천 오류 실행용
            val reco1_titleArray: ArrayList<String> = ArrayList<String>(10)
            for (i in 0..9) reco1_titleArray.add("Toy Story")
            var reco1_posterArray: ArrayList<String> = ArrayList<String>(10)
            for (i in 0..9) reco1_posterArray.add("/t/p/w300_and_h450_bestv2/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg")

            val reco2_1_userId = "1"
            val reco2_2_userId = "2"
            val reco2_3_userId = "3"
            val reco2_4_userId = "4"
            val reco2_5_userId = "5"

            var reco2_1_title: ArrayList<String> = ArrayList<String>(5)
            for (i in 0..4) reco2_1_title.add("Toy Story")
            var reco2_2_title: ArrayList<String> = ArrayList<String>(5)
            for (i in 0..4) reco2_2_title.add("Toy Story")
            var reco2_3_title: ArrayList<String> = ArrayList<String>(5)
            for (i in 0..4) reco2_3_title.add("Toy Story")
            var reco2_4_title: ArrayList<String> = ArrayList<String>(5)
            for (i in 0..4) reco2_4_title.add("Toy Story")
            var reco2_5_title: ArrayList<String> = ArrayList<String>(5)
            for (i in 0..4) reco2_5_title.add("Toy Story")
            
            var reco2_1_poster: ArrayList<String> = ArrayList<String>(5)
            for (i in 0..4) reco2_1_poster.add("/t/p/w300_and_h450_bestv2/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg")
            var reco2_2_poster: ArrayList<String> = ArrayList<String>(5)
            for (i in 0..4) reco2_2_poster.add("/t/p/w300_and_h450_bestv2/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg")
            var reco2_3_poster: ArrayList<String> = ArrayList<String>(5)
            for (i in 0..4) reco2_3_poster.add("/t/p/w300_and_h450_bestv2/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg")
            var reco2_4_poster: ArrayList<String> = ArrayList<String>(5)
            for (i in 0..4) reco2_4_poster.add("/t/p/w300_and_h450_bestv2/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg")
            var reco2_5_poster: ArrayList<String> = ArrayList<String>(5)
            for (i in 0..4) reco2_5_poster.add("/t/p/w300_and_h450_bestv2/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg")

            var reco3_titleArray: ArrayList<String> = ArrayList<String>(10)
            for (i in 0..9) reco3_titleArray.add("Toy Story")
            var reco3_posterArray: ArrayList<String> = ArrayList<String>(10)
            for (i in 0..9) reco3_posterArray.add("/t/p/w300_and_h450_bestv2/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg")

            var reco4_year: String = "1990"
            var reco4_titleArray: ArrayList<String> = ArrayList<String>(10)
            for (i in 0..9) reco4_titleArray.add("Toy Story")
            var reco4_posterArray: ArrayList<String> = ArrayList<String>(10)
            for (i in 0..9) reco4_posterArray.add("/t/p/w300_and_h450_bestv2/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg")

            var reco6_titleArray: ArrayList<String> = ArrayList<String>(10)
            for (i in 0..9) reco6_titleArray.add("Toy Story")
            var reco6_posterArray: ArrayList<String> = ArrayList<String>(10)
            for (i in 0..9) reco6_posterArray.add("/t/p/w300_and_h450_bestv2/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg")


            // 메인2로 이동
            var intent = Intent(applicationContext, MainActivity::class.java)
//            intent.putExtra("user_id", result.id) //id
//            intent.putExtra("user_name", result.name) //id
            intent.putExtra("user_id", id)
            intent.putExtra("user_name", id)

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

            // 서버에서 성공한 신호(응답)를 받으면 로딩창 종료
            progressDialog.dismiss()

            //Toast.makeText(this@LoginActivity, result.name + "님, 환영합니다.", Toast.LENGTH_SHORT).show()
            Toast.makeText(this@LoginActivity, id + "님, 환영합니다.", Toast.LENGTH_SHORT).show()

            startActivityForResult(intent, 0)
        */
        }

        // 양방향 액티비티 (회원가입 <-> 로그인)
        // 회원가입 버튼 클릭 - 회원가입 페이지로 이동
        btnSignup.setOnClickListener {
            var intent2 = Intent(applicationContext, SignupActivity_auth::class.java)
            startActivity(intent2)
        }
    }
}

class ProgressDialog1(context: Context?) : Dialog(context!!) {
    init {
        // 다이얼 로그 제목을 안보이게 설정
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_spinkit)

        // 라이브러리 로딩 이미지 사용 - CubeGrid
        val progressBar = findViewById<View>(R.id.spin_kit) as ProgressBar
        val cubeGrid: Sprite = CubeGrid()
        progressBar.indeterminateDrawable = cubeGrid
        textView.text = "당신의 취향을 분석중입니다..."
    }
}