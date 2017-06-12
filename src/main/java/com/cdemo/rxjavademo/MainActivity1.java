package com.cdemo.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import static rx.Observable.from;

/**
 * Created by yangdi on 2017/6/7.
 */

public class MainActivity1 extends AppCompatActivity {

    public static String TAG = "MainActivity1_RxJava_Test";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

//        function1();
        function2();
        function3();
    }


    /**
     * 变换
     * <p>
     * 例 把传进去的String类型参数转化为Bitmap
     * Observable.just("images/logo.png") // 输入类型 String
     * .map(new Func1<String, Bitmap>() {
     *
     * @Override public Bitmap call(String filePath) { // 参数类型 String
     * return getBitmapFromPath(filePath); // 返回类型 Bitmap
     * }
     * })
     * .subscribe(new Action1<Bitmap>() {
     * @Override public void call(Bitmap bitmap) { // 参数类型 Bitmap
     * showBitmap(bitmap);
     * }
     * });
     */
    private void function1() {

        // 这个例子把传进去的参数由String转换为String[]数组
        Observable.just("").map(new Func1<String, String[]>() {

            @Override
            public String[] call(String s) {
                return new String[]{"map"};
            }
        })

                .subscribe(new Action1<String[]>() {

                    @Override
                    public void call(String[] strings) {
                        Log.i(TAG, "call: " + strings[0]);
                    }
                });

        /**
         * 或者
         * Student[] students = ...;
         *
         Subscriber<String> subscriber = new Subscriber<String>() {
        @Override public void onNext(String name) {
        Log.d(tag, name);
        }
        ...
        };

         Observable.from(students)
         .map(new Func1<Student, String>() {
        @Override public String call(Student student) {
        return student.getName();
        }
        })
         .subscribe(subscriber);
         *
         */

        /**
         * 这里出现了一个叫做 Func1 的类。它和 Action1 非常相似，也是 RxJava 的一个接口，用于包装含有一个参数的方法。
         * Func1 和 Action 的区别在于， Func1 包装的是有返回值的方法。
         * 另外，和 ActionX 一样， FuncX 也有多个，用于不同参数个数的方法。FuncX 和 ActionX 的区别在 FuncX 包装的是有返回值的方法。
         */

    }

    // flatMap
    private void function2() {

        Students[] students = new Students[]{new Students("xiaoming", new Course[]{new Course("语文"),
                new Course("英语"), new Course("物理")}),
                new Students("xiaohu", new Course[]{new Course("政治"),
                        new Course("数学"), new Course("化学")})};

        // 一个学生有多门课程的情况
        // 订阅者1
        Subscriber<Course> subscribler = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                Log.i(TAG, "onNext: " + course.getName());
            }
        };


        Subscriber<Students> subscribler1 = new Subscriber<Students>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Students students) {

            }

        };


        // 被观察者1
        Observable observable = Observable
                .from(students)
                .flatMap(new Func1<Students, Observable<Course>>() {

                    @Override
                    public Observable<Course> call(Students students) {
                        return from(students.getCourse());
                    }
                });


        // 被观察者2
        Observable observable1 = Observable
                .from(students)
                .map(new Func1<Students, Observable<Course>>() {

                    @Override
                    public Observable<Course> call(Students students) {
                        return Observable.from(students.getCourse());
                    }
                });

        observable.subscribe(subscribler);


    }

    private void function3() {
    }
}
