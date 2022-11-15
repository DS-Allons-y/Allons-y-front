package com.example.harumub_front

import com.example.harumub_front.layout.GridVideoViewContainer
import kotlin.jvm.Volatile
//import com.example.harumub_front.layout.InChannelMessageListAdapter
import com.example.harumub_front.layout.SmallVideoViewAdapter
import android.view.ViewGroup.MarginLayoutParams
import com.example.harumub_front.propeller.ui.RecyclerItemClickListener
import io.agora.rtc.RtcEngine
import com.example.harumub_front.propeller.UserStatusData
import kotlin.jvm.Synchronized
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.harumub_front.layout.MessageListDecoration
import android.media.AudioManager
import android.content.res.Configuration
import android.os.*
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.example.harumub_front.*
import com.example.harumub_front.model.*
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.IRtcEngineEventHandler.RemoteVideoStats
import com.example.harumub_front.propeller.VideoInfoData
import io.agora.rtc.IRtcEngineEventHandler.AudioVolumeInfo
import com.example.harumub_front.propeller.ui.RtlLinearLayoutManager
import com.example.harumub_front.layout.SmallVideoViewDecoration
import com.example.harumub_front.propeller.Constant
import com.example.harumub_front.RetrofitBuilder
import com.example.harumub_front.RetrofitInterface
import com.example.harumub_front.WatchTogether
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
//import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


class TogetherActivity : BaseActivity(), DuringCallEventHandler {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInterface

    // 현재 로그인하고 있는 사용자 아이디, 이름
    lateinit var id : String
    private var myUid : Int = 0
    lateinit var roomCode : String
    lateinit var roomToken : String

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

    private val mUidsList = HashMap<Int?, SurfaceView?>() // uid = 0 || uid == EngineConfig.mUid
    var mLayoutType = LAYOUT_TYPE_DEFAULT
    private var mGridVideoViewContainer: GridVideoViewContainer? = null
    private var mSmallVideoViewDock: RelativeLayout? = null

    private var mVideoMuted = false
    private var mAudioMuted = false
    private var mAudioRouting = Constants.AUDIO_ROUTE_DEFAULT
    private var mFullScreen = false

    private var mIsLandscape = false
    private var mSmallVideoViewAdapter: SmallVideoViewAdapter? = null
    private val mUIHandler = Handler()

    private lateinit var emoji1 : ImageView
    private lateinit var emoji2 : ImageView
    private lateinit var emoji3 : ImageView
    private lateinit var emoji4 : ImageView
    private lateinit var eQueue : ArrayDeque<String> // 감정 4개 담을 큐 배열

    lateinit var cameraThread : CameraThread
    lateinit var cameraHandler : CameraHandler
    private var isChannelActivated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_together)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        id = intent.getStringExtra("user_id").toString()
        myUid = 10100 + Random.nextInt(900)
        roomCode = intent.getStringExtra("roomCode").toString()
        roomToken = intent.getStringExtra("roomToken").toString()

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

        emoji1 = findViewById<ImageView>(R.id.emotion1)
        emoji2 = findViewById<ImageView>(R.id.emotion2)
        emoji3 = findViewById<ImageView>(R.id.emotion3)
        emoji4 = findViewById<ImageView>(R.id.emotion4)

        eQueue = ArrayDeque<String>(4) // 감정 4개 담을 큐 배열
        for(i in 0 until 4){
            eQueue.add("Calm") // ["Calm"]
        } // ["Calm", "Calm", "Calm", "Calm"]
        Log.w("TogetherActivity", "감정 박스 초기화: "+eQueue)

        // 메인 페이지에서 전달받은 인텐트 데이터 확인
        if (intent.hasExtra("user_id") //&& intent.hasExtra("user_uid")
            && intent.hasExtra("roomCode") && intent.hasExtra("roomToken")) {
            Log.e("TogetherActivity", "입장에서 받아온 id : $id , " +
                    "\n사용자 uid: $myUid , " +
                    "\n방 코드: $roomCode , " +
                    "\n방 토큰: $roomToken")
        } else {
            Log.e("TogetherActivity", "가져온 데이터 없음")
        }

        cameraThread = CameraThread()
        cameraHandler = CameraHandler()
    }

    override fun initUIandEvent() {
        addEventHandler(this)
        val channelName = roomCode //intent.getStringExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME)

        // 이모티콘 이모지바
        val emoContainer = findViewById<LinearLayout>(R.id.emotions_container)
        val emoMp = emoContainer.layoutParams as ViewGroup.MarginLayoutParams
        emoMp.topMargin =
            statusBarHeight + actionBarHeight + resources.getDimensionPixelOffset(R.dimen.activity_vertical_margin) / 2 // status bar + action bar + divider

        val encryptionKey = intent.getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY)
        val encryptionMode = intent.getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE)
        doConfigEngine(encryptionKey, encryptionMode)
        mGridVideoViewContainer =
            findViewById<View>(R.id.grid_video_view_container) as GridVideoViewContainer
        mGridVideoViewContainer!!.setItemEventHandler(object :
            RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                onBigVideoViewClicked(view!!, position)
            }
            override fun onItemLongClick(view: View?, position: Int) {

            }
            override fun onItemDoubleClick(view: View?, position: Int) {
                onBigVideoViewDoubleClicked(view!!, position)
            }
        }
        )
        val surfaceV = RtcEngine.CreateRendererView(applicationContext)
        preview(true, surfaceV, 0)
        surfaceV.setZOrderOnTop(false)
        surfaceV.setZOrderMediaOverlay(false)
        mUidsList[0] = surfaceV // get first surface view
        mGridVideoViewContainer!!.initViewContainer(
            this,
            0,
            mUidsList,
            mIsLandscape
        ) // first is now full view

        Toast.makeText(this@TogetherActivity,
            "Channel Name: " + channelName + "\nUser: " + id,
            Toast.LENGTH_SHORT).show()


        if (channelName != null) {
            //val myUid = config().mUid
            //rtcEngine().joinChannel(roomToken, channelName, "1:N Group Call", myUid)
            rtcEngine().joinChannelWithUserAccount(roomToken, channelName, id)

            afterJoinChannel(channelName, myUid) //config().mUid)

            isChannelActivated = true
            Log.w("TogetherActivity", "ID: $id (UID: $myUid)님이 채널에 입장합니다. ")
            Thread.sleep(3000)

            // 채널에 들어왔으면 스레드 시작
            if(isChannelActivated) cameraThread.start()
        }
        optional()
    }

    inner class CameraThread : Thread() {

        var time = 0
        var isThreadActivated = true

        var id = intent.getStringExtra("user_id").toString()
        var roomCode = intent.getStringExtra("roomCode").toString()

        fun endThread() {
            isThreadActivated = false
            isChannelActivated = false
        }

        override fun run() {
            super.run()
            Log.w("TogetherActivity", "Thread 시작 - 캡처 대기")
            sleep(5000) // 페이지가 모두 로딩되기까지 충분히 기다리기

            while(isThreadActivated) {
                val message: android.os.Message = android.os.Message.obtain()
                message.what = SHARE_START
                //message.what = TogetherActivity.SHARE_START
                cameraHandler.sendMessage(message)

                // 캡처하기
                captureScreen("together", roomCode, time) //, id, roomCode, time

                time += 10
            }
        }
    }
    inner class CameraHandler : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)

            when (msg.what) {
                SHARE_START -> {
                    //TogetherActivity.SHARE_START -> {
                }
                //TogetherActivity.SHARE_END -> {
                SHARE_END -> {
                    cameraThread.endThread()
                }
                else -> {

                }
            }
        }
    }

    // 화면 캡처 > 이미지 파일 > s3버킷 전달
    private fun captureScreen(s3Bucket_FolderName: String, channelName: String, time: Int) { // , fileName: String, user_id: String, channelName: String, time: Int
        val fileName = channelName + "_" + id + "_" + time + ".jpg"
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.KOREA).format(Date())

        // 캡처하기
        rtcEngine().takeSnapshot(  //mRtcEngine?
            channelName,
            0,
            "/storage/emulated/0/Android/data/com.example.harumub_front/files/Pictures/$fileName" // $timeStamp.jpg
        )
        Log.w("TogetherActivity", "$timeStamp : $fileName - 사용자 비디오 캡처 완료")
        Thread.sleep(4000) // 저장하는 시간

        // s3 버킷에 올리기
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val outFile = File(storageDir, fileName)
        uploadWithTransferUtilty(s3Bucket_FolderName, fileName, outFile)

        // 실시간 라우터 - 감정 출력
        val map = HashMap<String, String>()
        map.put("id", id)
        map.put("roomCode", roomCode)
        //map.put("time", (time - 10).toString())
        map.put("time", time.toString())

        val call = retrofitInterface.executeWatchTogetherImageCapture(map)
        call!!.enqueue(object : Callback<WatchTogether?> {
            override fun onResponse(
                call: Call<WatchTogether?>,
                response: Response<WatchTogether?>
            ) {
                if (response.code() == 200) {
                    val result = response.body() // 감정 배열
                    var emotion_str = result!!.emotion_array // ['HAPPY', 'SAD']
                    emotion_str = emotion_str
                        .replace("[","")
                        .replace("]", "")
                        .replace("'", "")
                        .replace(" ","")
                    val emotion_array = emotion_str.split(",") // [HAPPY, SAD]
                    val size = emotion_array.size
                    Log.w("TogetherActivity", "감정 결과 - $time 초: $emotion_array" +
                            "\n감정 총 $size 개 받아왔습니다.")

                    shareEmotions(emotion_array, size, time)
                } else if (response.code() == 400) {
                    Log.e("TogetherActivity", "실시간 캡처/분석 오류")
//                    Toast.makeText(this@TogetherActivity, "실시간 캡처/분석 오류", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<WatchTogether?>, t: Throwable) {
                Log.e("TogetherActivity", t.message!!)
//                Toast.makeText(this@TogetherActivity, t.message,Toast.LENGTH_LONG).show()
            }
        })
        Thread.sleep(5000) // 캡처시 사진이 저장될 시간 + 버킷 올리는 시간 총 4~5초
    }

    // 감정 출력
    private fun shareEmotions(emotion_array: List<String>, size: Int, time: Int) {
        val emojis = intArrayOf( // 감정 이미지 Int 배열
            R.drawable.happy, R.drawable.sad, R.drawable.angry,
            R.drawable.confused, R.drawable.disgusted, R.drawable.surprised,
            R.drawable.fear, R.drawable.calm
        )
        val emotionIndex = arrayOf( // 감정 종류 String 배열
            "HAPPY", "SAD", "ANGRY", "CONFUSED", "DISGUSTED", "SURPRISED", "FEAR", "CALM"
        )
        //val eQueue = ArrayDeque<String>(4) // 감정 4개 담을 큐 배열
        if(size != 0) {
            for(i in 0 until size){
                eQueue.removeFirst()
                eQueue.add(emotion_array[i])
            }
        }
        Log.d("TogetherActivity", "감정 박스: " + eQueue)

        for (i in 0 until 4) {
            for(j in 0..7) {
                if (eQueue.elementAt(3-i) == emotionIndex[j]) { //emotion_array[i]
                    if (i == 0) runOnUiThread { emoji1.setImageResource(emojis[j]) }
                    else if (i == 1) runOnUiThread { emoji2.setImageResource(emojis[j]) }
                    else if (i == 2) runOnUiThread { emoji3.setImageResource(emojis[j]) }
                    else if (i == 3) runOnUiThread { emoji4.setImageResource(emojis[j]) }
                }
            }
        }
        Log.w("TogetherActivity", "$time 초: 감정 출력 완료")
    }

    private fun onBigVideoViewClicked(view: View, position: Int) {
        Log.d("TogetherActivity", "큰 비디오 뷰 클릭: onItemClick $view $position $mLayoutType")
        //log.debug("onItemClick $view $position $mLayoutType")
        toggleFullscreen()
    }

    private fun onBigVideoViewDoubleClicked(view: View, position: Int) {
        Log.d("TogetherActivity","큰 비디오 뷰 더블 클릭: onItemDoubleClick $view $position $mLayoutType")
        //log.debug("onItemDoubleClick $view $position $mLayoutType")
        if (mUidsList.size < 2) {
            return
        }
        val user = mGridVideoViewContainer!!.getItem(position)
        val uid = if (user.mUid == 0) config().mUid else user.mUid
        if (mLayoutType == LAYOUT_TYPE_DEFAULT && mUidsList.size != 1) {
            switchToSmallVideoView(uid)
        } else {
            switchToDefaultVideoView()
        }
    }

    private fun onSmallVideoViewDoubleClicked(view: View, position: Int) {
        Log.d("TogetherActivity","작은 비디오 뷰 더블 클릭: onItemDoubleClick small $view $position $mLayoutType")
        //log.debug("onItemDoubleClick small $view $position $mLayoutType")
        switchToDefaultVideoView()
    }

    private fun makeActivityContentShownUnderStatusBar() {
        // https://developer.android.com/training/system-ui/status
        // May fail on some kinds of devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            val decorView = window.decorView
            val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            decorView.systemUiVisibility = uiOptions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = resources.getColor(R.color.agora_blue)
            }
        }
    }

    private fun showOrHideStatusBar(hide: Boolean) {
        // May fail on some kinds of devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            val decorView = window.decorView
            var uiOptions = decorView.systemUiVisibility
            uiOptions = if (hide) {
                uiOptions or View.SYSTEM_UI_FLAG_FULLSCREEN
            } else {
                uiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
            }
            decorView.systemUiVisibility = uiOptions
        }
    }

    private fun toggleFullscreen() {
        mFullScreen = !mFullScreen
        showOrHideCtrlViews(mFullScreen)
        mUIHandler.postDelayed(
            { showOrHideStatusBar(mFullScreen) },
            200
        ) // action bar fade duration
    }

    private fun showOrHideCtrlViews(hide: Boolean) {
        val ab = supportActionBar
        if (ab != null) {
            if (hide) {
                ab.hide()
            } else {
                ab.show()
            }
        }
//        findViewById<View>(R.id.extra_ops_container).visibility =
        findViewById<View>(R.id.emotions_container).visibility =
            if (hide) View.INVISIBLE else View.VISIBLE
        findViewById<View>(R.id.bottom_action_container).visibility =
            if (hide) View.INVISIBLE else View.VISIBLE
//        findViewById<View>(R.id.msg_list).visibility =
//            if (hide) View.INVISIBLE else if (Constant.DEBUG_INFO_ENABLED) View.VISIBLE else View.INVISIBLE
    }

    private fun relayoutForVirtualKeyPad(orientation: Int) {
        val virtualKeyHeight = virtualKeyHeight()
        val emoContainer = findViewById<LinearLayout>(R.id.emotions_container)
        val emoMp = emoContainer.layoutParams as ViewGroup.MarginLayoutParams
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            emoMp.rightMargin = virtualKeyHeight
            emoMp.leftMargin = 0
        } else {
            emoMp.leftMargin = 0
            emoMp.rightMargin = 0
        }
        val bottomContainer = findViewById<LinearLayout>(R.id.bottom_container)
        val fmp = bottomContainer.layoutParams as ViewGroup.MarginLayoutParams
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fmp.bottomMargin = 0
            fmp.rightMargin = virtualKeyHeight
            fmp.leftMargin = 0
        } else {
            fmp.bottomMargin = virtualKeyHeight
            fmp.leftMargin = 0
            fmp.rightMargin = 0
        }
    }

//    @Synchronized
//    fun showCallOptions() {
//        val i = Intent(this, CallOptionsActivity::class.java)
//        startActivityForResult(i, CALL_OPTIONS_REQUEST)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CALL_OPTIONS_REQUEST) {
  //          Toast.makeText(this@TogetherActivity, data.toString(), Toast.LENGTH_SHORT).show()
//            val msgListView = findViewById<View>(R.id.msg_list) as RecyclerView
//            msgListView.visibility =
//                if (Constant.DEBUG_INFO_ENABLED) View.VISIBLE else View.INVISIBLE // Debug 정보 활성화 >> 보이기 / 비활성화>> 안보이기
        }
    }

    fun onClickHideIME(view: View) {
        Log.d("TogetherActivity","onClickHideIME $view")
            //log.debug("onClickHideIME $view")
        //closeIME(findViewById(R.id.msg_content))
//        findViewById<View>(R.id.msg_input_container).visibility = View.GONE // message list 뜨는 부분
        findViewById<View>(R.id.bottom_action_container).visibility = View.VISIBLE
    }

    private fun optional() {
        volumeControlStream = AudioManager.STREAM_VOICE_CALL
    }

    private fun optionalDestroy() {}

    // save the new value
    private val videoEncResolutionIndex: Int
        private get() {
            val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            var videoEncResolutionIndex = pref.getInt(
                ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_RESOLUTION,
                ConstantApp.DEFAULT_VIDEO_ENC_RESOLUTION_IDX
            )
            if (videoEncResolutionIndex > ConstantApp.VIDEO_DIMENSIONS.size - 1) {
                videoEncResolutionIndex = ConstantApp.DEFAULT_VIDEO_ENC_RESOLUTION_IDX

                // save the new value
                val editor = pref.edit()
                editor.putInt(
                    ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_RESOLUTION,
                    videoEncResolutionIndex
                )
                editor.apply()
            }
            return videoEncResolutionIndex
        }

    // save the new value
    private val videoEncFpsIndex: Int
        private get() {
            val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            var videoEncFpsIndex = pref.getInt(
                ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_FPS,
                ConstantApp.DEFAULT_VIDEO_ENC_FPS_IDX
            )
            if (videoEncFpsIndex > ConstantApp.VIDEO_FPS.size - 1) {
                videoEncFpsIndex = ConstantApp.DEFAULT_VIDEO_ENC_FPS_IDX

                // save the new value
                val editor = pref.edit()
                editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_FPS, videoEncFpsIndex)
                editor.apply()
            }
            return videoEncFpsIndex
        }

    private fun doConfigEngine(encryptionKey: String?, encryptionMode: String?) {
        val videoDimension = ConstantApp.VIDEO_DIMENSIONS[videoEncResolutionIndex]
        val videoFps = ConstantApp.VIDEO_FPS[videoEncFpsIndex]
        if (encryptionMode != null) {
            configEngine(videoDimension, videoFps, encryptionKey, encryptionMode)
        }
    }

    fun onSwitchCameraClicked(view: View?) {
        val rtcEngine = rtcEngine()
        // Switches between front and rear cameras.
        rtcEngine.switchCamera()

        Log.d("TogetherActivity","카메라 방향 전환 버튼 클릭")
    }

    fun onSwitchSpeakerClicked(view: View?) {
        val rtcEngine = rtcEngine()

        rtcEngine.setEnableSpeakerphone(mAudioRouting != Constants.AUDIO_ROUTE_SPEAKERPHONE)

        Log.d("TogetherActivity","스피커폰 on/off 버튼 클릭")
    }

    override fun onBackPressed() {
        // super.onBackPressed()
        // 주석처리 시 뒤로가기 버튼 막힘
    }

    override fun deInitUIandEvent() {
        cameraHandler.sendEmptyMessage(SHARE_END) // Together //thread 종료!
        Log.w("TogetherActivity", "onDestroy() > deInitUIandEvent() SHARE_END 메세지 전달 > thread 종료")

        optionalDestroy()
        doLeaveChannel()
        removeEventHandler(this)
        mUidsList.clear()
    }

    private fun doLeaveChannel() {
        cameraHandler.sendEmptyMessage(SHARE_END) // TogetherActivity.SHARE_END // isThreadActivated = false
        Log.w("TogetherActivity", "doLeaveChannel() SHARE_END 메세지 전달 > thread 종료")

        leaveChannel(config().mChannel!!)
        Log.w("TogetherActivity", "채널을 떠남")

        isChannelActivated = false

        preview(false, null, 0)
    }

    fun onHangupClicked(view: View) {
        Log.i("TogetherActivity","onHangupClicked $view")
            //log.info("onHangupClicked $view")

        cameraHandler.sendEmptyMessage(SHARE_END) // Together // thread 종료!
        Log.w("TogetherActivity", "종료 버튼 > SHARE_END 메세지 전달 > thread 종료")

        finish()
        Log.w("TogetherActivity", "통화 종료!")

        val map = HashMap<String, String>()
        map.put("id", id)
        map.put("roomCode", roomCode)
        val call = retrofitInterface.executeWatchTogetherEnd(map)
        call!!.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.code() == 200) {
                    Log.w("TogetherActivity", "Room & Room Code 삭제")

                    // 입장 페이지로 다시 돌아가기
                    finish()
                }
                else if (response.code() == 400) {
                    Log.w("TogetherActivity", "방 삭제 중 오류 발생")
                    Toast.makeText(this@TogetherActivity, "방 삭제 중 오류 발생",
                        Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Void?>, t: Throwable) {
                Log.e("TogetherActivity", t.toString())
                // Toast.makeText(this@TogetherActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    fun onVideoMuteClicked(view: View) {
        Log.i("TogetherActivity","비디오 on/off 버튼 클릭: onVoiceChatClicked " + view + " " + mUidsList.size + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted)
        //log.info("onVoiceChatClicked " + view + " " + mUidsList.size + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted)
        if (mUidsList.size == 0) {
            return
        }
        val surfaceV = localView
        var parent: ViewParent?
        if (surfaceV == null || surfaceV.parent.also { parent = it } == null) {
            Log.w("TogetherActivity","onVoiceChatClicked $view $surfaceV")
                //log.warn("onVoiceChatClicked $view $surfaceV")
            return
        }
        val rtcEngine = rtcEngine()
        mVideoMuted = !mVideoMuted
        if (mVideoMuted) {
            rtcEngine.disableVideo()
        } else {
            rtcEngine.enableVideo()
        }
        val iv = view as ImageView
        // iv.setImageResource(if (mVideoMuted) R.drawable.btn_camera_off else R.drawable.btn_camera)
        hideLocalView(mVideoMuted)
    }

    private val localView: SurfaceView?
        private get() {
            for ((key, value) in mUidsList) {
                if (key == 0 || key == config().mUid) {
                    return value
                }
            }
            return null
        }

    private fun hideLocalView(hide: Boolean) {
        val uid = config().mUid
        doHideTargetView(uid, hide)
    }

    private fun doHideTargetView(targetUid: Int, hide: Boolean) {
        val status = HashMap<Int?, Int?>()
        status[targetUid] = if (hide) UserStatusData.VIDEO_MUTED else UserStatusData.DEFAULT_STATUS
        if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
            mGridVideoViewContainer!!.notifyUiChanged(mUidsList, targetUid, status, null)
        } else if (mLayoutType == LAYOUT_TYPE_SMALL) {
            val bigBgUser = mGridVideoViewContainer!!.getItem(0)
            if (bigBgUser.mUid == targetUid) { // big background is target view
                mGridVideoViewContainer!!.notifyUiChanged(mUidsList, targetUid, status, null)
            } else { // find target view in small video view list
                Log.d("TogetherActivity","SmallVideoViewAdapter call notifyUiChanged " + mUidsList + " " + (bigBgUser.mUid and 0xFFFFFFFFL.toInt()) + " target: " + (targetUid and 0xFFFFFFFFL.toInt()) + "==" + targetUid + " " + status)
                    //log.warn("SmallVideoViewAdapter call notifyUiChanged " + mUidsList + " " + (bigBgUser.mUid and 0xFFFFFFFFL.toInt()) + " target: " + (targetUid and 0xFFFFFFFFL.toInt()) + "==" + targetUid + " " + status)
                mSmallVideoViewAdapter!!.notifyUiChanged(mUidsList, bigBgUser.mUid, status, null)
            }
        }
    }

    fun onVoiceMuteClicked(view: View) {
        Log.d("TogetherActivity","음소거 on/off 버튼 클릭: onVoiceMuteClicked " + view + " " + mUidsList.size + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted)
        //log.info("onVoiceMuteClicked " + view + " " + mUidsList.size + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted)
        if (mUidsList.size == 0) {
            return
        }
        val rtcEngine = rtcEngine()
        rtcEngine.muteLocalAudioStream(!mAudioMuted.also { mAudioMuted = it })
        val iv = view as ImageView
        // iv.setImageResource(if (mAudioMuted) R.drawable.btn_microphone_off else R.drawable.btn_microphone)
    }

    override fun onUserJoined(uid: Int) {
        Log.d("TogetherActivity","콜백함수: onUserJoined " + (uid and 0xFFFFFFFFL.toInt()))
            //log.debug("콜백함수: onUserJoined " + (uid and 0xFFFFFFFFL.toInt()))
        doRenderRemoteUi(uid)
        runOnUiThread {
            Toast.makeText(this@TogetherActivity,
                "User: $id (uid: $myUid ) joined!",
                Toast.LENGTH_SHORT).show()
//            notifyMessageChanged(
//                Message(
//                    User(0, null),
//                    "user " + (uid and 0xFFFFFFFFL.toInt()) + " joined"
//                )
//            )
        }
    }

    override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
        Log.d("TogetherActivity","콜백함수: onFirstRemoteVideoDecoded " + (uid and 0xFFFFFFFFL.toInt()) + " " + width + " " + height + " " + elapsed)
            //log.debug("콜백함수: onFirstRemoteVideoDecoded " + (uid and 0xFFFFFFFFL.toInt()) + " " + width + " " + height + " " + elapsed)
    }

    private fun doRenderRemoteUi(uid: Int) {
        runOnUiThread(Runnable {
            if (isFinishing) {
                return@Runnable
            }
            if (mUidsList.containsKey(uid)) {
                return@Runnable
            }

            val surfaceV = RtcEngine.CreateRendererView(applicationContext)
            mUidsList[uid] = surfaceV
            val useDefaultLayout = mLayoutType == LAYOUT_TYPE_DEFAULT
            surfaceV.setZOrderOnTop(true)
            surfaceV.setZOrderMediaOverlay(true)

            rtcEngine().setupRemoteVideo(
                VideoCanvas(
                    surfaceV,
                    VideoCanvas.RENDER_MODE_HIDDEN,
                    uid
                )
            )
            if (useDefaultLayout) {
                Log.d("TogetherActivity","doRenderRemoteUi LAYOUT_TYPE_DEFAULT " + (uid and 0xFFFFFFFFL.toInt()))
                    //log.debug("doRenderRemoteUi LAYOUT_TYPE_DEFAULT " + (uid and 0xFFFFFFFFL.toInt()))
                switchToDefaultVideoView()
            } else {
                val bigBgUid =
                    if (mSmallVideoViewAdapter == null) uid else mSmallVideoViewAdapter!!.exceptedUid
                Log.d("TogetherActivity","doRenderRemoteUi LAYOUT_TYPE_SMALL " + (uid and 0xFFFFFFFFL.toInt()) + " " + (bigBgUid and 0xFFFFFFFFL.toInt()))
                    //log.debug("doRenderRemoteUi LAYOUT_TYPE_SMALL " + (uid and 0xFFFFFFFFL.toInt()) + " " + (bigBgUid and 0xFFFFFFFFL.toInt()))
                switchToSmallVideoView(bigBgUid)
            }
            Toast.makeText(this@TogetherActivity,
                "Video from User: $id (uid: $myUid ) decoded!",
                Toast.LENGTH_SHORT).show()
//            notifyMessageChanged(
//                Message(
//                    User(0, null),
//                    "video from user " + (uid and 0xFFFFFFFFL.toInt()) + " decoded"
//                )
//            )
        })
    }

    override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
        Log.d("TogetherActivity","콜백함수: onJoinChannelSuccess " + channel + " " + (uid and 0xFFFFFFFFL.toInt()) + " " + elapsed)
            //log.debug("콜백함수: onJoinChannelSuccess " + channel + " " + (uid and 0xFFFFFFFFL.toInt()) + " " + elapsed)
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        Log.d("TogetherActivity","콜백함수: onUserOffline " + (uid and 0xFFFFFFFFL.toInt()) + " " + reason)
            //log.debug("콜백함수: onUserOffline " + (uid and 0xFFFFFFFFL.toInt()) + " " + reason)
        doRemoveRemoteUi(uid)
    }

    override fun onExtraCallback(type: Int, vararg data: Any) {
        runOnUiThread(Runnable {
            if (isFinishing) {
                return@Runnable
            }
            doHandleExtraCallback(type, *data)
        })
    }

    private fun doHandleExtraCallback(type: Int, vararg data: Any) {
        var peerUid: Int
        val muted: Boolean
        when (type) {
            AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED -> {
                peerUid = data[0] as Int
                muted = data[1] as Boolean
                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    val status = HashMap<Int?, Int?>()
                    status[peerUid] =
                        if (muted) UserStatusData.AUDIO_MUTED else UserStatusData.DEFAULT_STATUS
                    mGridVideoViewContainer!!.notifyUiChanged(
                        mUidsList,
                        config().mUid,
                        status,
                        null
                    )
                }
            }
            AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED -> {
                peerUid = data[0] as Int
                muted = data[1] as Boolean
                doHideTargetView(peerUid, muted)
            }
            AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS -> {
                val stats = data[0] as IRtcEngineEventHandler.RemoteVideoStats
                if (Constant.SHOW_VIDEO_INFO) {
                    if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                        mGridVideoViewContainer!!.addVideoInfo(
                            stats.uid,
                            VideoInfoData(
                                stats.width,
                                stats.height,
                                stats.delay,
                                stats.rendererOutputFrameRate,
                                stats.receivedBitrate
                            )
                        )
                        val uid = config().mUid
                        val profileIndex = videoEncResolutionIndex
                        val resolution =
                            resources.getStringArray(R.array.string_array_resolutions)[profileIndex]
                        val fps =
                            resources.getStringArray(R.array.string_array_frame_rate)[profileIndex]
                        val rwh = resolution.split("x".toRegex()).toTypedArray()
                        val width = Integer.valueOf(rwh[0])
                        val height = Integer.valueOf(rwh[1])
                        mGridVideoViewContainer!!.addVideoInfo(
                            uid, VideoInfoData(
                                if (width > height) width else height,
                                if (width > height) height else width,
                                0, Integer.valueOf(fps), Integer.valueOf(0)
                            )
                        )
                    }
                } else {
                    mGridVideoViewContainer!!.cleanVideoInfo()
                }
            }
            AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS -> {
                val infos = data[0] as Array<IRtcEngineEventHandler.AudioVolumeInfo>
                if (infos.size == 1 && infos[0].uid == 0) { // local guy, ignore it
                    //break // break 는 switch 문 안에서
                }
                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    val volume = HashMap<Int?, Int?>()
                    for (each in infos) {
                        peerUid = each.uid
                        val peerVolume = each.volume
                        if (peerUid == 0) {
                            continue
                        }
                        volume[peerUid] = peerVolume
                    }
                    mGridVideoViewContainer!!.notifyUiChanged(
                        mUidsList,
                        config().mUid,
                        null,
                        volume
                    )
                }
            }
            AGEventHandler.EVENT_TYPE_ON_APP_ERROR -> {
                val subType = data[0] as Int
                if (subType == ConstantApp.AppError.NO_CONNECTION_ERROR) {
                    val msg = getString(R.string.msg_connection_error)
                    //notifyMessageChanged(Message(User(0, null), msg))
  //                  showLongToast(msg)
                }
            }
            AGEventHandler.EVENT_TYPE_ON_DATA_CHANNEL_MSG -> {
                peerUid = data[0] as Int
                val content = data[1] as ByteArray
                //notifyMessageChanged(Message(User(peerUid, peerUid.toString()), String(content)))
                Toast.makeText(this@TogetherActivity,
                    "User(uid): $peerUid - " + String(content),
                    Toast.LENGTH_SHORT).show()
            }
            AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR -> {
                val error = data[0] as Int
                val description = data[1] as String
                //notifyMessageChanged(Message(User(0, null), "$error $description"))
//                Toast.makeText(this@CallActivity,
//                    "ERROR: $error $description",
//                    Toast.LENGTH_SHORT).show()
                Log.e("TogetherActivity","ERROR: $error $description")
                    //log.error("ERROR: $error $description")
            }
            AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED -> notifyHeadsetPlugged(data[0] as Int)
        }
    }

    private fun requestRemoteStreamType(currentHostCount: Int) {
        Log.d("TogetherActivity","requestRemoteStreamType $currentHostCount")
            //log.debug("requestRemoteStreamType $currentHostCount")
    }

    private fun doRemoveRemoteUi(uid: Int) {
        runOnUiThread(Runnable {
            if (isFinishing) {
                return@Runnable
            }
            val target = mUidsList.remove(uid) ?: return@Runnable
            var bigBgUid = -1
            if (mSmallVideoViewAdapter != null) {
                bigBgUid = mSmallVideoViewAdapter!!.exceptedUid
            }
            Log.d("TogetherActivity","doRemoveRemoteUi " + (uid and 0xFFFFFFFFL.toInt()) + " " + (bigBgUid and 0xFFFFFFFFL.toInt()) + " " + mLayoutType)
                //log.debug("doRemoveRemoteUi " + (uid and 0xFFFFFFFFL.toInt()) + " " + (bigBgUid and 0xFFFFFFFFL.toInt()) + " " + mLayoutType)
            if (mLayoutType == LAYOUT_TYPE_DEFAULT || uid == bigBgUid) {
                switchToDefaultVideoView()
            } else {
                switchToSmallVideoView(bigBgUid)
            }
            //notifyMessageChanged(Message(User(0, null), "user " + (uid and 0xFFFFFFFFL.toInt()) + " left"))
            Toast.makeText(this@TogetherActivity,
                "User(uid): $uid left ",
                Toast.LENGTH_SHORT).show()
        })
    }

    private fun switchToDefaultVideoView() {
        if (mSmallVideoViewDock != null) {
            mSmallVideoViewDock!!.visibility = View.GONE
        }
        mGridVideoViewContainer!!.initViewContainer(this, config().mUid, mUidsList, mIsLandscape)
        mLayoutType = LAYOUT_TYPE_DEFAULT
        var setRemoteUserPriorityFlag = false
        var sizeLimit = mUidsList.size
        if (sizeLimit > ConstantApp.MAX_PEER_COUNT + 1) {
            sizeLimit = ConstantApp.MAX_PEER_COUNT + 1
        }
        for (i in 0 until sizeLimit) {
            val uid = mGridVideoViewContainer!!.getItem(i).mUid
            if (config().mUid != uid) {
                if (!setRemoteUserPriorityFlag) {
                    setRemoteUserPriorityFlag = true
                    rtcEngine().setRemoteUserPriority(uid, Constants.USER_PRIORITY_HIGH)
                    Log.d("TogetherActivity","setRemoteUserPriority USER_PRIORITY_HIGH " + mUidsList.size + " " + (uid and 0xFFFFFFFFL.toInt()))
                        //log.debug("setRemoteUserPriority USER_PRIORITY_HIGH " + mUidsList.size + " " + (uid and 0xFFFFFFFFL.toInt()))
                } else {
                    rtcEngine().setRemoteUserPriority(
                        uid,
                        Constants.USER_PRIORITY_NORMAL
                    ) //USER_PRIORITY_NORANL
                    Log.d("TogetherActivity","setRemoteUserPriority USER_PRIORITY_NORANL " + mUidsList.size + " " + (uid and 0xFFFFFFFFL.toInt()))
                        //log.debug("setRemoteUserPriority USER_PRIORITY_NORANL " + mUidsList.size + " " + (uid and 0xFFFFFFFFL.toInt()))
                }
            }
        }
    }

    private fun switchToSmallVideoView(bigBgUid: Int) {
        val slice = HashMap<Int?, SurfaceView?>(1)
        slice[bigBgUid] = mUidsList[bigBgUid]
        //val iterator: Iterator<SurfaceView> = mUidsList.values.iterator()
        val iterator: MutableIterator<SurfaceView?> = mUidsList.values.iterator()
        while (iterator.hasNext()) {
            val s = iterator.next()
            s!!.setZOrderOnTop(true)
            s!!.setZOrderMediaOverlay(true)
        }
        mUidsList[bigBgUid]!!.setZOrderOnTop(false)
        mUidsList[bigBgUid]!!.setZOrderMediaOverlay(false)
        mGridVideoViewContainer!!.initViewContainer(this, bigBgUid, slice, mIsLandscape)
        bindToSmallVideoView(bigBgUid)
        mLayoutType = LAYOUT_TYPE_SMALL
        requestRemoteStreamType(mUidsList.size)
    }

    private fun bindToSmallVideoView(exceptUid: Int) {
        if (mSmallVideoViewDock == null) {
            val stub = findViewById<View>(R.id.small_video_view_dock) as ViewStub
            mSmallVideoViewDock = stub.inflate() as RelativeLayout
        }
        val twoWayVideoCall = mUidsList.size == 2
        val recycler = findViewById<View>(R.id.small_video_view_container) as RecyclerView
        var create = false
        if (mSmallVideoViewAdapter == null) {
            create = true
            mSmallVideoViewAdapter =
                SmallVideoViewAdapter(this, config().mUid, exceptUid, mUidsList)
            mSmallVideoViewAdapter!!.setHasStableIds(true)
        }
        recycler.setHasFixedSize(true)
        Log.d("TogetherActivity","bindToSmallVideoView " + twoWayVideoCall + " " + (exceptUid and 0xFFFFFFFFL.toInt()))
            //log.debug("bindToSmallVideoView " + twoWayVideoCall + " " + (exceptUid and 0xFFFFFFFFL.toInt()))
        if (twoWayVideoCall) {
            recycler.layoutManager =
                RtlLinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false) //RtlLinearLayoutManager.HORIZONTAL
        } else {
            recycler.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        }
        recycler.addItemDecoration(SmallVideoViewDecoration())
        recycler.adapter = mSmallVideoViewAdapter
        recycler.addOnItemTouchListener(
            RecyclerItemClickListener(
                baseContext,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {}
                    override fun onItemLongClick(view: View?, position: Int) {}
                    override fun onItemDoubleClick(view: View?, position: Int) {
                        onSmallVideoViewDoubleClicked(view!!, position)
                    }
                })
        )
        recycler.isDrawingCacheEnabled = true
        recycler.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_AUTO
        if (!create) {
            mSmallVideoViewAdapter!!.setLocalUid(config().mUid)
            mSmallVideoViewAdapter!!.notifyUiChanged(mUidsList, exceptUid, null, null)
        }
        for (tempUid in mUidsList.keys) {
            if (config().mUid != tempUid) {
                if (tempUid == exceptUid) {
                    rtcEngine().setRemoteUserPriority(tempUid, Constants.USER_PRIORITY_HIGH)
                    Log.d("TogetherActivity","setRemoteUserPriority USER_PRIORITY_HIGH " + mUidsList.size + " " + (tempUid and 0xFFFFFFFFL.toInt()))
                        //log.debug("setRemoteUserPriority USER_PRIORITY_HIGH " + mUidsList.size + " " + (tempUid and 0xFFFFFFFFL.toInt()))
                } else {
                    rtcEngine().setRemoteUserPriority(
                        tempUid!!,
                        Constants.USER_PRIORITY_NORMAL
                    ) //USER_PRIORITY_NORANL
                    Log.d("TogetherActivity","setRemoteUserPriority USER_PRIORITY_NORANL " + mUidsList.size + " " + (tempUid and 0xFFFFFFFFL.toInt()))
                        //log.debug("setRemoteUserPriority USER_PRIORITY_NORANL " + mUidsList.size + " " + (tempUid and 0xFFFFFFFFL.toInt()))
                }
            }
        }
        recycler.visibility = View.VISIBLE
        mSmallVideoViewDock!!.visibility = View.VISIBLE
    }

    fun notifyHeadsetPlugged(routing: Int) {
        Log.i("TogetherActivity","notifyHeadsetPlugged $routing $mVideoMuted")
            //log.info("notifyHeadsetPlugged $routing $mVideoMuted")
        mAudioRouting = routing
        val iv = findViewById<View>(R.id.switch_speaker_id) as ImageView
//        if (mAudioRouting == Constants.AUDIO_ROUTE_SPEAKERPHONE) {
//            iv.setImageResource(R.drawable.btn_speaker)
//        } else {
//            iv.setImageResource(R.drawable.btn_speaker_off)
//        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mIsLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
            switchToDefaultVideoView()
        } else if (mSmallVideoViewAdapter != null) {
            switchToSmallVideoView(mSmallVideoViewAdapter!!.exceptedUid)
        }
    }

    companion object {
        const val LAYOUT_TYPE_DEFAULT = 0
        const val LAYOUT_TYPE_SMALL = 1
        //private val log = LoggerFactory.getLogger(CallActivity::class.java)
        private const val CALL_OPTIONS_REQUEST = 3222

        private const val SHARE_START = 0
        private const val SHARE_END = 1
    }

    // S3 Bucket Upload
    fun uploadWithTransferUtilty(s3Bucket_FolderName: String?, fileName: String?, file: File?) {
        val awsCredentials: AWSCredentials =
            BasicAWSCredentials(getString(R.string.AWS_ACCESS_KEY), getString(R.string.AWS_SECRET_KEY)) // IAM User의 (accessKey, secretKey)
        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2))
        val transferUtility =
            TransferUtility.builder().s3Client(s3Client).context(this.applicationContext).build()
        TransferNetworkLossHandler.getInstance(this.applicationContext)

        if (file != null) {
            if (file.exists()) { // 파일 객체가 실제로 존재하면 버킷에 올리기
                val uploadObserver =
                    transferUtility.upload("allonsybucket1/$s3Bucket_FolderName", fileName, file) // (bucket name, file 이름, file 객체)
                uploadObserver.setTransferListener(object : TransferListener {
                    override fun onStateChanged(s3_id: Int, state: TransferState) {
                        if (state === TransferState.COMPLETED) {
                            // Handle a completed upload
                            Log.d("Together_S3 Bucket", "Upload Completed!")
                            Log.w("Together_S3 Bucket", "$fileName >> 버킷에 업로드 완료!")

                            // S3 Bucket에 file 업로드 후 Emulator에서 삭제
                            file.delete()
                            Log.w("Together_Emulator", "파일 삭제 완료")
                        }
                    }

                    override fun onProgressChanged(id: Int, current: Long, total: Long) {
                        val done = (current.toDouble() / total * 100.0).toInt()
                        Log.d("TogetherActivity", "UPLOAD - - ID: $id, percent done = $done")
                    }

                    override fun onError(id: Int, ex: java.lang.Exception) {
                        Log.d("TogetherActivity", "UPLOAD ERROR - - ID: $id - - EX:$ex")
                    }
                })
            }
            else {
                Log.e("Together_Emulator", "업로드할 파일이 없습니다.")
            }
        }
    }
}