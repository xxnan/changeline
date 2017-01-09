dependencies {
添加compile 'com.vanke.changeline:changeline:2.0.0'
}
style.xml
定义方向，从左边还是右边
<resources>
    <declare-styleable name="direction">
        <attr name="direct" format="integer" />
    </declare-styleable>
</resources>


layout.xml

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:xxnan="http://schemas.android.com/apk/res-auto"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
   >


    <com.vanke.changeline.ChangeLine
        android:id="@+id/changeLine"
        xxnan:direct="1"//0代表左边开始，1代表右边开始
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        >
		 <TextView
            style="@style/text_flag"
            android:text="facebook" />
			 <TextView
            style="@style/text_flag"
            android:text="facebook" />
			.....
			.....
			.....
 </com.vanke.changeline.ChangeLine>

</LinearLayout>



