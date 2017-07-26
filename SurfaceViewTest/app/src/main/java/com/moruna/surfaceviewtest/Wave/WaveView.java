package com.moruna.surfaceviewtest.Wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

import com.moruna.surfaceviewtest.R;

/**
 * Author: Moruna
 * Date: 2017-07-26
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 * Desc:录音波纹
 */
public class WaveView extends RenderView{

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    private final Paint paint = new Paint();
    {
        paint.setDither(true);
        paint.setAntiAlias(true);
    }

    //第一条轨迹
    private final Path firstPath = new Path();

    //第二条轨迹
    private final Path secondPath = new Path();

    //振幅比较低的两条
    private final Path centerPath = new Path();
    private final Path centerPath2 = new Path();

    //采样点的数量，越高越精细，但有瓶颈
    private static final int SAMPLING_SIZE = 60;

    //采样点的X
    private float[] samplingX;

    //采样点位置均匀映射到[-2,2]的X集合
    private float[] mapX;

    // 画布宽高
    private int width, height;

    // 画布中心的高度
    private int centerHeight;

    //振幅
    private int amplitude;

    //音量大小回调参数
    private float rmsdB = 1;

    //交叉点数目
    private static int CROSS_POINT_NUM = 10;

    /**
     * 波峰和两条路径交叉点的记录，包括起点和终点，用于绘制渐变。
     * 通过日志可知其数量范围为7~10个，故这里size取10。
     *
     * 每个元素都是一个float[2]，用于保存xy值
     */
    private final float[][] crestAndCrossPints = new float[CROSS_POINT_NUM][];
    {//直接分配内存
        for (int i = 0; i < CROSS_POINT_NUM; i++) {
            crestAndCrossPints[i] = new float[2];
        }
    }


    @Override
    protected void onRender(Canvas canvas, long millisPassed) {
        if (samplingX == null) {//首次初始化
            width = canvas.getWidth();
            height = canvas.getHeight();
            centerHeight = height >> 1;//高的一半
            amplitude = width >> 6;//振幅为宽度的2的六次方

            //初始化采样点和映射
            samplingX = new float[SAMPLING_SIZE + 1];//因为包括起点和终点所以需要+1个位置
            mapX = new float[SAMPLING_SIZE + 1];//同上
            float gap = width / (float) SAMPLING_SIZE;//确定采样点之间的间距
            float x;
            for (int i = 0; i <= SAMPLING_SIZE; i++) {
                x = i * gap;
                samplingX[i] = x;
                mapX[i] = (x / (float) width) * 4 - 2;//将x映射到[-2,2]的区间上
            }
        }

        //绘制背景
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        //重置所有path并移动到起点
        firstPath.rewind();
        secondPath.rewind();
        centerPath.rewind();
        centerPath2.rewind();
        firstPath.moveTo(0, centerHeight);
        secondPath.moveTo(0, centerHeight);
        centerPath.moveTo(0, centerHeight);
        centerPath2.moveTo(0, centerHeight);

        //当前时间的偏移量，通过该偏移量使得每次绘图都向右偏移，让画面动起来。如果希望速度快一点，可以调小分母
        float offset = millisPassed / 430F;

        //临时参数
        float x;
        float[] xy;

        //波形函数的值，包括上一点，当前点和下一点
        float lastV, curV = 0, nextV = (float) (amplitude * calcValue(mapX[0], offset));
        //波形函数的绝对值，用于筛选波峰和交错点
        float absLastV, absCurV, absNextV;
        //上一个筛选出的点是波峰还是交错点
        boolean lastIsCrest = false;
        //筛选出的波峰和交叉点的数量，包括起点和终点
        int crestAndCrossCount = 0;

        //遍历所有采样点
        for (int i = 0; i <= SAMPLING_SIZE; i++) {
            //计算采样点的位置
            x = samplingX[i];
            lastV = curV;
            curV = nextV;
            //提前算出下一采样点的值
            nextV = i < SAMPLING_SIZE ? (float) (amplitude * calcValue(mapX[i + 1], offset)) : 0;

            //连接路径
            firstPath.lineTo(x, centerHeight + curV);
            secondPath.lineTo(x, centerHeight - curV);
            //中间那条路径的振幅是上下的1/3
            centerPath.lineTo(x, centerHeight + curV / 3F);
            centerPath2.lineTo(x, centerHeight - curV / 3F);

            //记录极值点
            absLastV = Math.abs(lastV);
            absCurV = Math.abs(curV);
            absNextV = Math.abs(nextV);
            crestAndCrossCount = 9;
            if (i == 0 || i == SAMPLING_SIZE/*起点终点*/ || (lastIsCrest && absCurV < absLastV && absCurV < absNextV)/*上一个点为波峰，且该点是极小值点*/) {
                if (crestAndCrossCount >= CROSS_POINT_NUM-1) {
                    continue;
                }
                xy = crestAndCrossPints[crestAndCrossCount++];
                xy[0] = x;
                xy[1] = 0;
                lastIsCrest = false;
            } else if (!lastIsCrest && absCurV > absLastV && absCurV > absNextV) {/*上一点是交叉点，且该点极大值*/
                if (crestAndCrossCount >= CROSS_POINT_NUM-1) {
                    continue;
                }
                xy = crestAndCrossPints[crestAndCrossCount++];
                xy[0] = x;
                xy[1] = curV;
                lastIsCrest = true;
            }
        }
        //连接所有路径到终点
        firstPath.lineTo(width, centerHeight);
        secondPath.lineTo(width, centerHeight);
        centerPath.lineTo(width, centerHeight);
        centerPath2.lineTo(width, centerHeight);

        //清理一下
        paint.setShader(null);
        paint.setXfermode(null);

        //绘制上弦线
        paint.setStrokeWidth(2f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setAlpha(255);
        canvas.drawPath(firstPath, paint);

        //绘制下弦线
        paint.setStrokeWidth(1.6f);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setAlpha(220);
        canvas.drawPath(secondPath, paint);


        //绘制中间线
        paint.setStrokeWidth(1.4f);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setAlpha(180);
        canvas.drawPath(centerPath, paint);

        paint.setStrokeWidth(1.3f);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setAlpha(140);
        canvas.drawPath(centerPath2, paint);
    }

    /**
     * 计算波形函数中x对应的y值
     *
     * @param mapX   换算到[-2,2]之间的x值
     * @param offset 偏移量
     * @return
     */
    private double calcValue(float mapX, float offset) {
        offset %= 2;
        double sinFunc = Math.sin(0.75 * Math.PI * mapX - offset * Math.PI);
        double recessionFunc = Math.pow(4 / (4 + Math.pow(mapX, 2)), 2.5);
        return sinFunc * recessionFunc * rmsdB;
    }
}
