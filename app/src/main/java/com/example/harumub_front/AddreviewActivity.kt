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
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.CubeGrid
import kotlinx.android.synthetic.main.activity_add_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap
import kotlin.properties.Delegates

class AddreviewActivity : AppCompatActivity() {

    private lateinit var progressDialog : ProgressDialog    // 로딩 다이얼로그

    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInterface

    private var isPlayed by Delegates.notNull<Boolean>()

    private lateinit var myTitle: TextView
    private lateinit var myGenres: TextView
    private lateinit var myPoster: ImageView

    var defaultImage = R.drawable.default_poster // 포스터 기본 이미지

    // 현재 로그인하고 있는 사용자 아이디, 선택한 영화 아이디, 선택한 영화의 장르, 선택한 영화의 포스터
    lateinit var id : String
    lateinit var movie_title : String
    lateinit var genres : String
    lateinit var poster : String

    // 추천 정보
    lateinit var reco1_titleArray : java.util.ArrayList<String>
    lateinit var reco1_posterArray : java.util.ArrayList<String>

    lateinit var reco2_1_userId : String
    lateinit var reco2_2_userId : String
    lateinit var reco2_3_userId : String
    lateinit var reco2_4_userId : String
    lateinit var reco2_5_userId : String

    lateinit var reco2_1_title : java.util.ArrayList<String>
    lateinit var reco2_2_title : java.util.ArrayList<String>
    lateinit var reco2_3_title : java.util.ArrayList<String>
    lateinit var reco2_4_title : java.util.ArrayList<String>
    lateinit var reco2_5_title : java.util.ArrayList<String>

    lateinit var reco2_1_poster : java.util.ArrayList<String>
    lateinit var reco2_2_poster : java.util.ArrayList<String>
    lateinit var reco2_3_poster : java.util.ArrayList<String>
    lateinit var reco2_4_poster : java.util.ArrayList<String>
    lateinit var reco2_5_poster : java.util.ArrayList<String>

    lateinit var reco3_titleArray : java.util.ArrayList<String>
    lateinit var reco3_posterArray : java.util.ArrayList<String>

    lateinit var reco4_year : String
    lateinit var reco4_titleArray : ArrayList<String>
    lateinit var reco4_posterArray : ArrayList<String>

    lateinit var reco6_titleArray : ArrayList<String>
    lateinit var reco6_posterArray : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)
        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        id = intent.getStringExtra("user_id").toString()
        movie_title = intent.getStringExtra("movie_title").toString()
        genres = intent.getStringExtra("genres").toString()
        poster = intent.getStringExtra("poster").toString()

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
        reco4_titleArray = intent.getSerializableExtra("reco4_titleArray") as ArrayList<String>
        reco4_posterArray = intent.getSerializableExtra("reco4_posterArray") as ArrayList<String>

        reco6_titleArray = intent.getSerializableExtra("reco6_titleArray") as ArrayList<String>
        reco6_posterArray = intent.getSerializableExtra("reco6_posterArray") as ArrayList<String>

        isPlayed = intent.getBooleanExtra("isPlayed", false)

        // 혼자보기 페이지에서 전달받은 인텐트 데이터 확인
        if (intent.hasExtra("user_id") && intent.hasExtra("movie_title")) {
            Log.d("AddReviewActivity", "받아온 id : $id , movie title : $movie_title" +
                    "\n받아온 genres : $genres , poster : $poster")
        } else {
            Log.e("AddReviewActivity", "가져온 데이터 없음")
        }

        // 감상했던 영화 제목
        myTitle = findViewById<TextView>(R.id.title)
        myTitle.setText(movie_title)

        // 영화 장르 - String으로 받아옴 >> 문자열 자르기
        myGenres = findViewById<TextView>(R.id.genres)
        genres = genres
            .replace("[","")
            .replace("]", "")
            .replace("'", "")
            .replace(" ","")
        println("부호,공백 > 제거 : $genres") // Action,Fantasy,Family

        val arrGenres = genres.split(',') // 반점 기준 단어 분리
        var result = ""
        val size = arrGenres.size
        println("장르 총 개수: $size")
        for(i in 0 until size) { // i: 0 ~ (size-1)
            if (i == (size-1)) { // 마지막이면 반점 추가 X
                result += arrGenres[i]
            } else {
                result = result + arrGenres[i] + ", "
            }
            println("장르: $result")
        }
        myGenres.setText(result)

        // 포스터
        myPoster = findViewById<ImageView>(R.id.poster)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500" + poster) // 불러올 이미지 url
            .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImage) // 로드할 url이 비어있을(null 등) 경우 표시할 이미지
            .into(myPoster) // 이미지를 넣을 뷰


        // 별점 - 람다식을 사용하여 처리
        ratingBar.setOnRatingBarChangeListener{ ratingBar, rating, fromUser ->
            ratingBar.rating = rating
            //Toast.makeText(applicationContext, "별점: ${rating}", Toast.LENGTH_SHORT).show()
        }

        // 로딩창 선언
        progressDialog = ProgressDialog(this)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 백그라운드를 투명하게

        // 등록하기 버튼 클릭시 별점, 한줄평 >> 결과 페이지&서버에 전달, 로딩 다이얼로그 출력
        btnAdd.setOnClickListener {
            // 로딩창 실행
            // progressDialog.setCancelable(false) // 외부 클릭으로 다이얼로그 종료 X - 실행 위해 임시로 주석 처리
            progressDialog.show() // 로딩화면 보여주기

            var user_comment = comment.text.toString() // EditText 입력값을 텍스트로

            var map = HashMap<String, String>()
            map.put("id", id!!)
            map.put("movieTitle", movie_title)
            map.put("rating", ratingBar.rating.toString())
            map.put("comment", user_comment)

            val call = retrofitInterface.executeAddReview(map)
            call!!.enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                    if (response.code() == 200) {
                        Toast.makeText(this@AddreviewActivity, "리뷰 보내기 성공", Toast.LENGTH_SHORT).show()

                        if(response.code() == 200) {
                            // 서버에서 감상 결과를 불러오는 데 성공한 신호(응답)를 받으면 로딩창 종료
                            progressDialog.dismiss()

                            var intent = Intent(applicationContext, ResultActivity_ticket_front::class.java)
                            intent.putExtra("user_id", id)
                            intent.putExtra("movie_title", movie_title)

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

                            intent.putExtra("isPlayed", isPlayed)

                            startActivityForResult(intent, 0)
                        }
                    }
                    else if (response.code() == 400) {
                        //Toast.makeText(this@AddreviewActivity, "오류 발생", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    //Toast.makeText(this@AddreviewActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun onBackPressed() { // 뒤로 가기 버튼 막기
        //super.onBackPressed()
    }
}

class ProgressDialog(context: Context?) : Dialog(context!!) {
    init {
        // 다이얼 로그 제목을 안보이게 설정
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_spinkit)

        // 라이브러리 로딩 이미지 사용 - CubeGrid
        val progressBar = findViewById<View>(R.id.spin_kit) as ProgressBar
        val cubeGrid: Sprite = CubeGrid()
        progressBar.indeterminateDrawable = cubeGrid
    }
}