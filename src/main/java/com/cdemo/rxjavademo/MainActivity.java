package com.cdemo.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainActivity_RxJava_Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        function1();
//        function2();

//        function3();

        startActivity(new Intent(MainActivity.this, MainActivity1.class));


    }


    /**
     * RxJava基本使用
     *
     * 传递Strig类型字符串
     */
    private void function1() {

        // 创建观察者
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: " + s);
            }
        };

        // 也可通过Subscriber创建观察者
        Subscriber<String> subscribler = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "Subscriber onCompleted: onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "Subscriber onNext: " + s);
            }

        };

        // 创建被观察者第一种方式
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("str");
            }

        });

        // 下面两种方式都是按顺序执行参数
        Observable<String> observable1 = Observable.just("str1", "str2", "str3");
        String str[] = new String[]{"str3", "str4", "str5"};
        // 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
        Observable<String> observable2 = Observable.from(str);

        // 订阅
        observable.subscribe(observer);
        observable.subscribe(subscribler);
        observable1.subscribe(observer);
        observable2.subscribe(subscribler);

    }

    /**
     * RxJava不完整回调、写法
     */
    private void function2() {


        /**
         *  Action0 是 RxJava 的一个接口，它只有一个方法 call()，这个方法是无参无返回值的；
         *  由于 onCompleted() 方法也是无参无返回值的，因此 Action0 可以被当成一个包装对象，
         *  将 onCompleted() 的内容打包起来将自己作为一个参数传入 subscribe() 以实现不完整定义的回调。
         *  这样其实也可以看做将 onCompleted() 方法作为参数传进了 subscribe()，相当于其他某些语言中的『闭包』。
         *  Action1 也是一个接口，它同样只有一个方法 call(T param)，这个方法也无返回值，但有一个参数；
         *  与 Action0 同理，由于 onNext(T obj) 和 onError(Throwable error) 也是单参数无返回值的，
         *  因此 Action1 可以将 onNext(obj) 和 onError(error) 打包起来传入 subscribe() 以实现不完整定义的回调。
         *  事实上，虽然 Action0 和 Action1 在 API 中使用最广泛，但 RxJava 是提供了多个 ActionX 形式的接口 (例如 Action2, Action3) 的，
         *  它们可以被用以包装不同的无返回值的方法。
         */
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "action1 call: " + s);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
            }
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Log.d(TAG, "completed");
            }
        };


        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("不完整定义回调");
            }
        });

        // 自动创建观察者
        observable.subscribe(onNextAction);
        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);
        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);


        // 可以以下简洁写法
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        });
    }


    /**
     * 线程控制 Scheduler调度器
     *
     *
     Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
     Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
     Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
     Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
     另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
     */
    private void function3() {

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("线程控制Scheduler");
            }
        }).subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i(TAG, "Scheduler call: "+s);
                    }
                });

    }


}
