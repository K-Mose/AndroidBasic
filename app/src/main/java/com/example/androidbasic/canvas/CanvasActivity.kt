package com.example.androidbasic.canvas

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import com.example.androidbasic.databinding.ActivityCanvasBinding

/*
https://www.journaldev.com/25182/android-canvas
Canvas는 2D drawing Framework이다. 우리에게 비트맵에 기초한 그림을 그릴 수 있는 메소드들을 제공한다.
비트맵은 캔버스가 놓여지는 위 표면처럼 행동한다. Paint 클래스는 컬러와 스타일을 제공하는데 사용된다.
캔버스의 lifecycle
커스텀 뷰는 일반적으로 사용되는 메서드들로 구성되어있다.
onMeasure() - 뷰와 뷰의 자식들의 사이즈를 정의한다.
onLayout() - 뷰에게 할당되는 사이즈
onDraw() - 캔버스가 그려지는 곳의 메서드
캔버스 객체는 기본적으로 onDraw() 메서드 안에서 파라메터로 들어온다.
invalidate() 메서드는 뷰를 다시 그리기 위해 사용된다. 이것은 onDraw() 메서드를 다시 부른다.
일반적으로, 이것은 텍스트, 색상 또는 뷰가 특정 이벤트들을 기반으로 업데이트를 필요로 할 때 사용된다.

Canvas 클래스는 선, 호, 원, 등등 을 포함하는 모양을 그리는 메서드들을 가지고 있다.
더욱이 우리는 Paths를 사용함으로써 복잡한 모양을 그릴 수 있다.
또한 글씨를 쓰거나 단순하게 색을 칠할 수 있다.

 * 캔버스가 스크린위에 그림을 그릴 때, 그려지는 뷰는 픽셀 단위(terms of pixels)를 측정을 필요로 한다.

어떻게 그려지는지 보자
구조 : 캔버스로 그려질 액티비티, 캔버스를 그릴 View()를 상속하는 클래스

Path를 만들기 위해서 moveTo()와 lineTo() 메서드가 중요하다.
moveTo() - 스크린 위에 특정 좌표를 가진다.
lineTo() - 현재 위치에서 특정 위치까지 선을 그린다.
close() - path를 닫는다

캔버스 좌표 시스템
왼쪽 제일 위가 (0,0)이 된다.
오른쪽으로 가면 x 좌표가,아래로 내려가면 y 좌표가 커진다.
가로모드와 세로모드 각각에서 따로 계산된다.

Rect vs RectF
Rect 객체는 사각형을 생성하기 위해 사용된다.
Rect는 Integer 값을 받고, RectF는 Float 값을 받는다.

 */
class CanvasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCanvasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCanvasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // DrawingCanvas 클래스 안에 onDraw()에서 캔버스에 그려지는 뷰를 추가한다.
        binding.linearLayout.addView(DrawingCanvas(this@CanvasActivity))
    }
}