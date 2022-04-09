call "C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Auxiliary\Build\vcvarsall.bat" amd64
call C:\d\jvm\graal17\bin\native-image.cmd ^
-H:+ReportExceptionStackTraces ^
--report-unsupported-elements-at-runtime ^
--no-fallback ^
--allow-incomplete-classpath ^
-H:ReflectionConfigurationFiles=config/reflect-config.json ^
-H:JNIConfigurationFiles=config/jni-config.json ^
-H:DynamicProxyConfigurationFiles=config/proxy-config.json ^
-H:SerializationConfigurationFiles=config/serialization-config.json ^
-H:ResourceConfigurationFiles=config/resource-config.json ^
-Dorg.lwjgl.librarypath=. ^
-jar noisegen-0.1.2.jar