package com.example.androidbasic.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidbasic.databinding.ActivityCoroutineNetworkRequestBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
// https://developer.android.com/kotlin/coroutines
class CoroutineNetworkRequest : AppCompatActivity() {
    lateinit var binding: ActivityCoroutineNetworkRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutineNetworkRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // NetworkOnMainThreadException
        binding.apply {
            tvText.setOnClickListener{
                LoginViewModel().login()
            }
        }

    }

/*
login function
viewModelScope -  ViewModel KTX 확장이 포함되어있는 미리 정의된 코루틴 스코프
launch - 코루틴을 생성하고 launch의 바디에서 응답하는 디스패처의 실행을 보내는 함수
Dispatchers.IO - 입출력 작업으로 위해 준비된 스레드에서 실행되기 위한 코루틴을 지칭함
login function 실행 순서
 1. 앱은 login 함수를 View 계층 위에 메인 스레드에서부터 호출한다.
 2. launch는 새로운 코루틴을 생성하고, I/O 작업을 위해 준비된 스레드에서 네트워크 리궤스트가 생성된다.
 3. 코루틴이 작동되는 동안, login 함수는 가능한 네트워크가 끝나기 전까지 실행하고 반환한다.

코루틴이 viewModelScope에서 실행될 때 부터, ViewModel 범위에서 실행되어진다.
유저가 다른 스크린으로 떠나서 ViewModel이 파괴되어 진다면, viewModelScope는 자동적으로 취소된다.
그리고 실행중이던 코루틴도 취소된다.

UI가 실행될 때 메인 스레드 안에서 실행되므로 makeLoginRequest는 UI을 막지 않게 main이 아닌 다른 스레드
즉, IO스레드에서 실행되어야 한다.
withContext()를 사용함으로서 코루틴의 실행을 다른 스레드에서 실행시킬 수 있다.
*/

    inner class LoginViewModel:ViewModel(){

        /*
        코루틴이 LoginViewModel에서 생성될 때
        makeLoginReques는 메인 스레드 밖에서 실행되고,
        login 함수안에 코루틴은 메인 스레드 안에서 실행된다.
         */
        fun login(){
            viewModelScope.launch {
                // 예외처를 하기 위해서 Repository 계층에서 코틀린에 내장된 예외처리를 사용해서 예외를 던질 수 있다.
                // 수정) try-catch 추가
                val result = try{ //
                    LoginRepository().makeLoginRequest("{\"a\":\"b\"}")
                }catch (e: Exception){ // 예외처리 시키기
                    e.printStackTrace()
                    ERROR
                }
                Log.d("RESULT",result)
                when (result) {
                    SUCCESS -> binding.tvText.text = SUCCESS
                    FAIL -> binding.tvText.text = FAIL
                    ERROR -> binding.tvText.text = ERROR
                }
            }
        }
    }

    class LoginRepository(){
        // Mocky에서 생성한  API 주소
        private val uri = "https://run.mocky.io/v3/468a6471-5a59-44ad-9f6d-946355cf9d6e"
        // suspend 키워드가 추가된다. 이 키워드는 함수가 코루틴 안에서 호출될 것이라는 것을 나타낸다.
        // 모든 suspend 함수는 코루틴안에서 실행되어야만 한다.
        suspend fun makeLoginRequest(
                jsonBody: String
        ): String{
            val url = URL(uri)
            /*
            withContext(Dispatchers.IO)는 코루틴의 실행을 I/O 스레드로 옮긴다.
            UI를 실행할 때 main-safe하게 된다.
             */
            return withContext(Dispatchers.IO) {
                (url.openConnection() as HttpURLConnection).run {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json; utf-8")
                    setRequestProperty("Accept", "application/json")
                    doOutput = true
                    outputStream.write(jsonBody.toByteArray())
                    if(responseCode == HttpURLConnection.HTTP_OK){
                        inputStream.use {
                            it.reader().use { reader ->
                                reader.readText()
                            }
                        }
                    }else{
                        "connection fail"
                    }
                }
            }
        }

    }
/*
main-safe에서 달라진점
  - launch는 Dispathcers.IO를 받지 않는다. launch가 Dispatcher를 받지 않을 때,
    viewModelScope에서 실행되는 코루틴들은 메인 스레드 안에서 실행된다.
  - 네트웨크 성공 또는 실피의 리퀘스트 결과를 UI에 조작 가능하다.
  > I/O 스레드 안에서는 UI를 건드릴 수 없고 Main에서만 가능하다. (에러 발생)

login 함수는 다음 항목들을 실행시킬 수 있다.
  - 앱이 login함수를 메인스레드의 View 계층에서 부를 수 있다.
  - launch 새로운 네트워크 요청 코루틴을 메인 스레드 위에 생성 가능하고, 실행시킬 수 있다.
  - 코루틴 내부에서, loginRepository.makeLoginRequest()의 호출은 이제
    makeLoginRequest()안에 withContext 블록이 실행을 종료시킬 때까지 코루틴의 실행을 더 지연시킨다.
  - withContext 블록이 끝나게되면,
    login() 안에 코루틴은 네트워크 요청의 결과와 함께 메인 스레드를 다시 실행시킨다.

* Noet: ViewModel 계층에서 View와 통신을 하기 위해서,
        https://developer.android.com/jetpack/docs/guideLiveData에서 권장하는 LiveData를 사용하라
        위의 패턴을 사용할 때, ViewModel 안에 코드는 메인 스레드 안에서 실행되고,
        MutalbleLiveData의 setValue() 함수를 직접 호출할 수 있다.


*/

    companion object{
        private const val ERROR = "ERROR OCCURRED!"
        private const val SUCCESS = "SUCCESS!"
        private const val FAIL = "CONNECTION FAILED.."
    }
}