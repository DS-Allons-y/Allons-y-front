package com.example.harumub_front

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WatchListAdapter(var id: String, var titles: ArrayList<String>, var posters: ArrayList<String>,
                       var reco1_titleArray: ArrayList<String>, var reco1_posterArray: ArrayList<String>,
                       var reco2_1_userId: String, var reco2_2_userId: String, var reco2_3_userId: String,
                       var reco2_4_userId: String, var reco2_5_userId: String,
                       var reco2_1_title : ArrayList<String>, var reco2_2_title : ArrayList<String>, var reco2_3_title : ArrayList<String>,
                       var reco2_4_title : ArrayList<String>, var reco2_5_title : ArrayList<String>,
                       var reco2_1_poster : ArrayList<String>, var reco2_2_poster : ArrayList<String>, var reco2_3_poster : ArrayList<String>,
                       var reco2_4_poster : ArrayList<String>, var reco2_5_poster : ArrayList<String>,
                       var reco3_titleArray : ArrayList<String>, var reco3_posterArray : ArrayList<String>,
                       var reco4_year : String, var reco4_titleArray : ArrayList<String>, var reco4_posterArray : ArrayList<String>,
                       var reco6_titleArray : ArrayList<String>, var reco6_posterArray : ArrayList<String>):
    RecyclerView.Adapter<WatchListAdapter.ViewHolder>() {

    var defaultImage = R.drawable.default_poster
    var isPlayed = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WatchListAdapter.ViewHolder {
        // RecyclerView에 들어갈 아이템의 레이아웃 설정
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_layout, parent, false)
        return ViewHolder(v)
    }

    // 순서에 따라 배열에 데이터 삽입
    override fun onBindViewHolder(holder: WatchListAdapter.ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + posters[position]) // 불러올 이미지 url
            .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImage) // 로드할 url이 비어있을(null 등) 경우 표시할 이미지
            .into(holder.movieImage) // 이미지를 넣을 뷰

        holder.movieTitle.text = titles[position]
    }

    override fun getItemCount(): Int {
        return titles.size // 영화 개수
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var movieImage: ImageView
        var movieTitle: TextView

        init {
            movieImage = itemView.findViewById(R.id.movie_image) // 영화 이미지 버튼
            movieTitle = itemView.findViewById(R.id.movie_title) // 영화 제목

            // 해당 아이템 클릭시 결과 페이지로 이동
            itemView.setOnClickListener {
                val position: Int = adapterPosition

                //Toast.makeText(itemView.context, titles[position], Toast.LENGTH_LONG).show() // 영화 클릭 시 토스트 메시지

                var movie_title = titles[position]
                val intent = Intent(itemView.context, ResultActivity_ticket_front::class.java)
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

                itemView.context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }
}