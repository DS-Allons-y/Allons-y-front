package com.example.harumub_front

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import kotlinx.android.synthetic.main.fragment_result_ticket_back.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.properties.Delegates


class ResultActivity_ticket_front : AppCompatActivity() {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface: RetrofitInterface

    // 현재 로그인하고 있는 사용자 아이디, 선택한 영화 제목, 별점 평가, 한줄평
    lateinit var id : String
    lateinit var movie_title : String

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

    // 티켓 누르면 ResultActivity_ticket_back.kt 로 이동하도록 하기 위함
    lateinit var totalTicket : RelativeLayout
    lateinit var myTitle : TextView

    var defaultImage = R.drawable.default_poster

    // 서버에서 받아올 데이터 정의
    lateinit var result_date : String
    lateinit var result_title : String
    lateinit var result_poster : String
    lateinit var result_genres : String
    lateinit var result_concentration : String
    lateinit var result_highlight_time :String
    lateinit var result_emotion_count_array : ArrayList<Emotion>
    lateinit var result_highlight_array : ArrayList<Highlight>
    lateinit var result_rating : Number
    lateinit var result_comment : String
    var result_isRemaked by Delegates.notNull<Boolean>()
    lateinit var result_remake_title : String
    lateinit var result_remake_poster : String

    // ResultActivity_ticket_back에 전달해 줄 배경색
    lateinit var result_background_color : Number

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_result_ticket_front)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        // initialize~~ 오류 발생해서 추가한 코드.
        result_date = ""
        result_title = ""
        result_poster = ""
        result_genres = ""
        result_concentration = ""
        result_highlight_time = ""
        result_emotion_count_array = arrayListOf()
        result_highlight_array = arrayListOf()
        result_rating = 0
        result_comment = ""
        result_background_color = 0
        result_isRemaked = false
        result_remake_title = ""
        result_remake_poster = ""

        id = intent.getStringExtra("user_id").toString()
        movie_title = intent.getStringExtra("movie_title").toString()

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

        // 리뷰 페이지에서 전달받은 인텐트 데이터 확인
        if (intent.hasExtra("user_id") && intent.hasExtra("movie_title")) {
            Log.d("ResultActivity",
                "리뷰에서 받아온 id : " + id + " movie_title : " + movie_title)
        }
        else {
            Log.e("ResultActivity", "가져온 데이터 없음")
        }

        // ResultActicity_ticket_front에 표시할 데이터(날짜, 포스터, 장르 ~ )
        var myDate = findViewById<TextView>(R.id.date)
        var myPoster = findViewById<ImageView>(R.id.movie_image)
        var myGenres = findViewById<TextView>(R.id.genre)
        var myBackground = findViewById<RelativeLayout>(R.id.background)
        myTitle = findViewById(R.id.title)

        // ticket 전체 부분
        totalTicket = findViewById(R.id.total_ticket)

        // 사용자 감상 목록으로 이동하는 버튼
        var btnList = findViewById<ImageButton>(R.id.back2list)


        var map = HashMap<String, String>()
        map.put("id", id)
        map.put("movieTitle", movie_title)

        val call = retrofitInterface.executeWatchResult(map)
        call!!.enqueue(object : Callback<WatchResult?> {
            override fun onResponse(call: Call<WatchResult?>, response: Response<WatchResult?>) {
                if (response.code() == 200) {
                    //Toast.makeText(this@ResultActivity, "결과 출력 성공", Toast.LENGTH_SHORT).show()

                    val result = response.body()
                    Log.d("감상 영화 정보 : ", "제목 : " + result!!.title
                            + " 장르 : " + result.genres + " 집중 : " + result.concentration
                            + " 하이라이트 시간 : " + result.highlight_time
                            + " 별점 : " + result.rating + " 한줄평 : " + result.comment
                            + " 감상 날짜 : " + result.date
                            + " 리메이크 여부 : " + result.remake + " 리메이크 작품 : " + result.remakeTitle
                            + " 리메이크 포스터 : " + result.remakePoster)

                    // 서버에서 받아온 데이터 초기화
                    result_date = result.date
                    result_title = result.title
                    result_poster = result.poster
                    result_genres = result.genres
                    result_concentration = result.concentration
                    result_highlight_time = result.highlight_time
                    result_emotion_count_array = result.emotion_count_array
                    result_highlight_array = result.highlight_array
                    result_rating = result.rating
                    result_comment = result.comment
                    result_isRemaked = result.remake
                    result_remake_title = result.remakeTitle
                    result_remake_poster = result.remakePoster


                    // 감상했던 영화 정보 불러오기 - 제목
                    myTitle.text = result_title
                    myDate.text = result_date

                    // 영화 장르 - String으로 받아옴 >> 문자열 자르기
                    var genres = result_genres
                    genres = genres
                        .replace("[","")
                        .replace("]", "")
                        .replace("'", "")
                        .replace(" ","")
                    println("부호,공백 > 제거 : $genres") // Action,Fantasy,Family

                    val arrGenres = genres.split(',') // 반점 기준 단어 분리
                    var total = ""
                    val arrSize = arrGenres.size
                    println("장르 총 개수: $arrSize")

                    myGenres.text = arrGenres[0]

                    // 영화 포스터 출력 - 웹에서 url로 가져오기
                    val posterUrl = result_poster

                    Glide.with(applicationContext)
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/w500" + posterUrl) // 불러올 이미지 u
                        .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url이 비어있을(null 등) 경우 표시할 이미지
                        .into(object : CustomTarget<Bitmap>(){
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                            ) { // 포스터 평균색에 맞춰 뒷배경 색 변경
                                myPoster.setImageBitmap(resource)
                                val myPalette:Palette = Palette.from(resource).generate()
                                val dominantSwatch: Palette.Swatch = myPalette!!.getDominantSwatch()!!
                                myBackground.setBackgroundColor(dominantSwatch.rgb)
                                Log.e("rgb", dominantSwatch.rgb.toString())
                                result_background_color = dominantSwatch.rgb

                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                TODO("Not yet implemented")
                            }
                        }) // 이미지를 넣을 뷰

                    // ResultActivity_ticket_back.kt로 이동
                    totalTicket.setOnClickListener(ticketClick())
                }
                else if (response.code() == 400) {
                    //Toast.makeText(this@ResultActivity, "오류 발생", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<WatchResult?>, t: Throwable) {
                //Toast.makeText(this@ResultActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })

        // 리스트 목록으로 이동하는 버튼
        btnList.setOnClickListener {
            var intent = Intent(
                applicationContext,
                WatchListActivity::class.java
            ) // 두번째 인자에 이동할 액티비티
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

            startActivityForResult(intent, 0)
        }
    }

    private fun ticketClick(): View.OnClickListener? { // 티켓(앞) 누르면 티켓(뒤)로 넘어감
        return View.OnClickListener() {
            totalTicket.animate().rotationY(180f).setDuration(300).withEndAction{ // 애니메이션
                totalTicket.translationY = 0f
                val intent = Intent(totalTicket.context, ResultActivity_ticket_back::class.java)

                intent.putExtra("user_id", id)
                intent.putExtra("movie_title", myTitle.text)

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

                intent.putExtra("date", result_date)
                intent.putExtra("title", result_title)
                intent.putExtra("poster", result_poster)
                intent.putExtra("genres", result_genres)
                intent.putExtra("concentration", result_concentration)
                intent.putExtra("highlight_time", result_highlight_time)
                intent.putExtra("emotion_count_array", result_emotion_count_array)
                intent.putExtra("highlight_array", result_highlight_array)
                intent.putExtra("rating", result_rating)
                intent.putExtra("comment", result_comment)
                intent.putExtra("remake", result_isRemaked)
                intent.putExtra("remakeTitle", result_remake_title)
                intent.putExtra("remakePoster", result_remake_poster)

                intent.putExtra("background_color", result_background_color)


                totalTicket.context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

            }.start()
        }
    }
}
