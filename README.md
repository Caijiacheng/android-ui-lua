android-ui-lua
==============

This is one project with library and example to show how to test the Android UI with the Lua Script. 


Project description:
    This is the simple example to show how to test the Android UI with the LUA.
    test/test-robot-lib-app-instr/script has the simple test file of lua

    
    
Project Tree:
    root -- lib -- 
                    luajava-apklib :  
                                The Project to Ndk the libs of lua engine for Android 
                    robot-apklib : 
                                The Library with wrapper LuaEngine and Instrumentation and others
                                you can add more operations to mapping in IOper.java and IView.java and so on...
            test -- 
                    test-robot-lib-app:
                                The project of app
                    test-robot-lib-app-instr:
                                The Project of instr app to test-robot-lib-app.

HOW TO RUN the Test:
    INSTALL:
        1) install depends:
            MAVEN: 
                http://maven.apache.org/
            ANDROID SDK:
                http://developer.android.com/sdk/index.html            
            ANDROID NDK:
                http://developer.android.com/tools/sdk/ndk/index.html
        2) Set MAVEN Depends Path:
            ANDROID_HOME = ${sdk_path}
            ANDROID_NDK_HOME = ${ndk_path}
            
        3) Device:
            1) usb link to device
            2) device has sdcard to test
            
        3) RUN:
            mvn clean install

            
            
QA:
    1. why Lua to UI Script?
        1) java and eclipse is too heavy to test Android UI which is changing frequencies.
        
    2. dex failed?
        if the target app to instr has the dependencies of slf4j and logback, it would be conficted . 
        1) make <scope> to tag of PROVIDED and retry. if failed, go 2)
        2) remove the dependencies of slf4j and logback
