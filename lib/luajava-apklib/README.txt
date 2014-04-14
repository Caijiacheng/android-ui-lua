
LUAJAVA_APKLIB:


eclipse中import maven工程后，不会主动加入编译JNI的NDK目录。所以，要想在eclipse中调试，需要加入NDK_BUILD
具体流程，参考：

    http://www.cnblogs.com/chenjiajin/archive/2012/04/12/2444188.html
    
配置结束后，重新clean，生成一下。理论上，luajava-apklib -> libs 下面会有armeabi, x86的生成目录

