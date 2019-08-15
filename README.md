# FloatingWindows
简易的悬浮窗控制台输出控件

## 使用说明
### 一：添加依赖
####  (1).首先下载aar放入app-libs
####  (2).app->build.gradle添加如下代码
      repositories  {
       flatDir {
           dirs 'libs'
       }
      }
####  (3).在dependencies中添加（consolelog-release根据aar文件名而定，由于使用了joda的时间处理类所以需要在项目中加入该时间引用类）：
    implementation "net.danlew:android.joda:2.10.2"
    implementation(name:'consolelog-release',ext:'aar')

### 二：在activity中检查权限并开启服务
   
      if(!Settings.canDrawOverlays(this)){
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        }else{
            startService(new Intent(MainActivity.this, LoggingService.class));

        }
### 三：使用
                ConsoleLogger.i(TAG,"T:"+mname+" this is infomation!"+ DateTimeUtil.dateToStr(new Date()));
                ConsoleLogger.e(TAG,"T:"+mname+" this is error!"+ DateTimeUtil.dateToStr(new Date()));
                ConsoleLogger.d(TAG,"T:"+mname+" this is debug!"+DateTimeUtil.dateToStr(new Date()));
                ConsoleLogger.v(TAG,"T:"+mname+" This is VERBOSE!"+DateTimeUtil.dateToStr(new Date()));
                ConsoleLogger.w(TAG,"T:"+mname+" this is warn!"+DateTimeUtil.dateToStr(new Date()));
                ConsoleLogger.wtf(TAG,"T:"+mname+" this is WTF!"+DateTimeUtil.dateToStr(new Date()));
                ConsoleLogger.println("123123");
                ConsoleLogger.e(TAG,new Throwable());
                
### 关于这个“小玩具”的一些问题
####    1.输出颜色：
#####       最开始的时候设计是可以输出颜色，使用的是SpannableStringBuilder进行动态改变颜色，后来在性能测试时候发现一个问题，textview的性能很让人着急（可能是我使用的不熟练吧）在频繁的setText（）后会造成UI卡顿（相当严重），但是若是不使用SpannableStringBuilder就没这个问题，后来跟踪栈发现是Textview内部大量的反射获取对象时造成了卡顿问题，所以目前只能先放弃使用颜色，后期等解决了性能问题再上颜色吧。

####    2.性能问题：
#####       由于“小玩具”一般就是内部测试使用，所以性能这块没有太多的关注，使用的全局handler进行信息发送，这个后期根据实际情况应该会重新设计。
            
####    3.关于模块：
#####       我只简单的封装了一下，其他使用请自行修改，本模块在不进行频繁的log输出的情况下正常使用应该没什么问题，若是大批量log日志输出，还是使用其他更为成熟的日志框架吧，设计初衷就是为了监控APP中某一特定位置运行是否正常，也由于textview的一些限制太频繁的刷新势必会造成性能问题，所以在关键位置输出关键点才是最主要的。
    

